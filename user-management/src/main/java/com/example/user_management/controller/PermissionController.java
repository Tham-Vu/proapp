package com.example.user_management.controller;

import com.example.user_management.entity.User;
import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.request.PermissionModel;
import com.example.user_management.model.request.UserModel;
import com.example.user_management.model.response.CommonResponseModel;
import com.example.user_management.service.PermissionService;
import com.example.user_management.service.UserService;
import com.example.user_management.utils.AESUtil;
import com.example.user_management.utils.Consts;
import com.example.user_management.utils.LoggerInfo;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PermissionController {
    private static final Logger LOGGER = Logger.getLogger(PermissionController.class);
    private final PermissionService service;
    private final UserService userService;
    private final Gson gson;

    public PermissionController(PermissionService service, UserService userService, Gson gson) {
        this.service = service;
        this.userService = userService;
        this.gson = gson;
    }
    @GetMapping("/permissions")
    public ResponseEntity<?> getAllPermission(){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "getAllPermission", "/permission", null, null, startDate));

        List<PermissionModel> list = new ArrayList<>();
        CommonResponseModel res = null;
        try{
            list = service.getAllPermission();
            if (list == null || list.isEmpty()){
                LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getAllPermission", "/permissions",  null, null, startDate, new Date(), Consts.NO_USER_FOUND_IN_DATABASE));
                res = new CommonResponseModel(HttpStatus.NO_CONTENT.value(), Consts.NO_USER_FOUND_IN_DATABASE);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
            }
        }catch(Exception e){
            LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getAllPermission", "/permissions",  null, null, startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getAllPermission", "/permissions",  null, null, startDate, new Date(), gson.toJson(list)));
        String jData = AESUtil.encrypt(gson.toJson(list), currentUser==null?null:currentUser.getSecretCode());
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), jData );
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PreAuthorize("hasRole('GET_PERMISSION_BY_ID')")
    @GetMapping("/permissions/{id}")
    public ResponseEntity<?> getPermissionById(@PathVariable long id){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "getPermission", "/permissions/" + id, null, String.valueOf(id), startDate));

        PermissionModel permissionRes = null;
        CommonResponseModel res = null;
        try {
            permissionRes = service.getPermissionById(id);
        } catch (BadRequestException e) {
            LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getPermissionById", "/permissions/"+ id,  null, String.valueOf(id), startDate, new Date(), e.getMessage() + id));
            res = new CommonResponseModel(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getPermissionById", "/permissions/"+ id,  null, String.valueOf(id), startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }

        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getPermissionById", "/permissions/"+ id,  null, String.valueOf(id), startDate, new Date(),permissionRes.toString()));
        String jData = AESUtil.encrypt(String.valueOf(permissionRes), currentUser==null?null:currentUser.getSecretCode());
        res = new CommonResponseModel(String.valueOf(startDate), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), jData);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PostMapping("/permissions")
    public ResponseEntity<?> createPermission(@RequestBody PermissionModel model){
        Date startDate = new Date();
        User currentUser =  userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "createPermission", "/permissions" , null, model.toString(), startDate));
        PermissionModel permissionRes = null;
        CommonResponseModel res = null;
        try{
            permissionRes = service.savePermission(model);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "createUser", "/users",  null, model.toString(), startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "createUser", "/users",  null, model.toString(), startDate, new Date(), permissionRes.toString()));
        String jData = AESUtil.encrypt(gson.toJson(permissionRes), currentUser==null?null:currentUser.getSecretCode());
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), permissionRes.toString());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PutMapping("/permissions")
    public ResponseEntity<?> updatePermission(@RequestBody PermissionModel permissionModel){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "updateUser", "/users", null, null, startDate));
        PermissionModel resPermission = null;
        CommonResponseModel res = null;
        try{
            resPermission = service.savePermission(permissionModel);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "updatePermission", "/permissions",  null, permissionModel.toString(), startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "updatePermission", "/permission",  null, permissionModel.toString(), startDate, new Date(), resPermission.toString()));
        String jData = AESUtil.encrypt(gson.toJson(resPermission), currentUser==null?null:currentUser.getSecretCode());
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), jData);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable long id){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "deletePermission", "/permissions", null, null, startDate));
        CommonResponseModel res = null;
        try{
            service.deletePermission(id);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "deletePermission", "/permissions",  null, String.valueOf(id), startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "deletePermissions", "/permissions",  null, String.valueOf(id), startDate, new Date(), Consts.DELETE_PERMISSION_SUCCESSFULLY + id));
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
