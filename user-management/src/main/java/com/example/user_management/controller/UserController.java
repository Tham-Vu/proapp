package com.example.user_management.controller;

import com.example.user_management.entity.User;
import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.request.LoginModel;
import com.example.user_management.model.request.UserModel;
import com.example.user_management.model.response.CommonResponseModel;
import com.example.user_management.model.response.TokenResponse;
import com.example.user_management.service.UserService;
import com.example.user_management.utils.AESUtil;
import com.example.user_management.utils.Consts;
import com.example.user_management.utils.JWTUtils;
import com.example.user_management.utils.LoggerInfo;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    public static final Logger LOGGER = Logger.getLogger(UserController.class);
    private final UserService userService;
    private final Gson gson;
    private final UserDetailsService userDetailsService;
    private final JWTUtils jwtUtils;

    public UserController(UserService userService, Gson gson, UserDetailsService userDetailsService, JWTUtils jwtUtils) {
        this.userService = userService;
        this.gson = gson;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "getUserById", "/users/" + id, null, String.valueOf(id), startDate));

        UserModel userModel = null;
        CommonResponseModel res = null;
        try {
            userModel = userService.getUserById(id);
        } catch (BadRequestException e) {
            LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getUserById", "users/"+ id,  null, String.valueOf(id), startDate, new Date(), e.getMessage() + id));
            res = new CommonResponseModel(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getUserById", "users/"+ id,  null, String.valueOf(id), startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }

        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getUserById", "users/"+ id,  null, String.valueOf(id), startDate, new Date(), userModel.toString()));
        String jData = AESUtil.encrypt(String.valueOf(userModel), currentUser==null?null:currentUser.getSecretCode());
        res = new CommonResponseModel(String.valueOf(startDate), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), jData);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "getAllUsers", "/users", null, null, startDate));

        List<UserModel> list = new ArrayList<>();
        CommonResponseModel res = null;
        try{
            list = userService.getAllUser();
            if (list == null || list.isEmpty()){
                LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getAllUsers", "users",  null, null, startDate, new Date(), Consts.NO_USER_FOUND_IN_DATABASE));
                res = new CommonResponseModel(HttpStatus.NO_CONTENT.value(), Consts.NO_USER_FOUND_IN_DATABASE);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
            }
        }catch(Exception e){
            LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getAllUsers", "users",  null, null, startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getAllUsers", "users",  null, null, startDate, new Date(), gson.toJson(list)));
        String jData = AESUtil.encrypt(gson.toJson(list), currentUser==null?null:currentUser.getSecretCode());
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), jData );
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserModel userModel){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        String username = null;
        String secretCode = null;
        List authorities = new ArrayList();
        if (currentUser != null){
            username = currentUser.getUsername();
            authorities = (List) currentUser.getAuthorities();
            secretCode = currentUser.getSecretCode();
        }
        LOGGER.info(new LoggerInfo(username, "createUser", "/users", null, null, startDate));
        UserModel resUser = null;
        CommonResponseModel res = null;
         try{
             resUser = userService.saveUser(userModel);
         }catch (Exception e){
             LOGGER.error(new LoggerInfo(username, authorities.stream().map(role -> role.toString()).toString(), "createUser", "/users",  null, userModel.toString(), startDate, new Date(), e.getMessage()));
             res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
         }
        LOGGER.error(new LoggerInfo(username, authorities.stream().map(role -> role.toString()).toString(), "createUser", "/users",  null, userModel.toString(), startDate, new Date(), resUser.toString()));
//        String jData = AESUtil.encrypt(gson.toJson(resUser), null);
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), resUser.toString());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody UserModel userModel){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "updateUser", "/users", null, null, startDate));
        UserModel resUser = null;
        CommonResponseModel res = null;
        try{
            resUser = userService.saveUser(userModel);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "updateUser", "/users",  null, userModel.toString(), startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "updateUser", "/users",  null, userModel.toString(), startDate, new Date(), resUser.toString()));
        String jData = AESUtil.encrypt(gson.toJson(resUser), currentUser==null?null:currentUser.getSecretCode());
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), jData);
        return ResponseEntity.status(HttpStatus.OK).body(resUser);
    }
    @PatchMapping("/users/change-active")
    public ResponseEntity<?> changeActive(@RequestBody Long id){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser.getUsername(), "changeActive", "/users/change-active", null, null, startDate));
        UserModel resUser = null;
        CommonResponseModel res = null;
        try{
            resUser = userService.changeActive(id);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser.getUsername(), currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "changeActive", "/users/change-active",  null, id.toString(), startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        LOGGER.error(new LoggerInfo(currentUser.getUsername(), currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "changeActive", "/users/change-active",  null, id.toString(), startDate, new Date(), resUser.toString()));
        String jData = AESUtil.encrypt(gson.toJson(resUser), currentUser.getSecretCode());
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), jData);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser.getUsername(), "deleteUser", "/users", null, null, startDate));
        CommonResponseModel res = null;
        try{
            userService.deleteUser(id);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser.getUsername(), currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "deleteUser", "/users",  null, id.toString(), startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        LOGGER.error(new LoggerInfo(currentUser.getUsername(), currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "deleteUser", "/users",  null, id.toString(), startDate, new Date(), Consts.DELETE_USER_SUCCESSFULLY + id));
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginModel loginModel){
        User user = (User) userDetailsService.loadUserByUsername(loginModel.getUsername());
        String accessToken = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccess_token(accessToken);
        tokenResponse.setRefresh_token(refreshToken);
        tokenResponse.setToken_type(Consts.BEARER.trim());
        tokenResponse.setExpires_in(String.valueOf(Consts.JWT_EXPIRE));
        System.out.println("Hello World!");
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }

}
