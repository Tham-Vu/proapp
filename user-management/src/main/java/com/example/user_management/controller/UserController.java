package com.example.user_management.controller;

import com.example.user_management.entity.User;
import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.request.UserModel;
import com.example.user_management.service.UserService;
import com.example.user_management.utils.Consts;
import com.example.user_management.utils.LoggerInfo;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class UserController {
    public static final Logger LOGGER = Logger.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser.getUsername(), "getUserById", "/users/" + id, null, String.valueOf(id), startDate));

        UserModel userModel = null;
        try {
            userModel = userService.getUserById(id);
        } catch (BadRequestException e) {
            LOGGER.info(new LoggerInfo(currentUser.getUsername(), currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getUserById", "users/"+ id,  null, String.valueOf(id), startDate, new Date(), e.getMessage() + id));
            throw new RuntimeException(e);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser.getUsername(), currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getUserById", "users/"+ id,  null, String.valueOf(id), startDate, new Date(), e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        LOGGER.info(new LoggerInfo(currentUser.getUsername(), currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getUserById", "users/"+ id,  null, String.valueOf(id), startDate, new Date(), userModel.toString()));
        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser.getUsername(), "getAllUsers", "/users", null, null, startDate));

        List<UserModel> list = new ArrayList<>();
        try{
            list = userService.getAllUser();
            if (list == null || list.isEmpty()){
                LOGGER.info(new LoggerInfo(currentUser.getUsername(), currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getAllUsers", "users",  null, null, startDate, new Date(), Consts.NO_USER_FOUND_IN_DATABASE));
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body();
            }
        }
    }

}
