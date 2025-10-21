package com.example.user_management.controller;

import com.example.user_management.entity.User;
import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.request.GroupModel;
import com.example.user_management.model.request.PermissionModel;
import com.example.user_management.model.response.CommonResponseModel;
import com.example.user_management.service.GroupsService;
import com.example.user_management.service.UserService;
import com.example.user_management.utils.AESUtil;
import com.example.user_management.utils.Consts;
import com.example.user_management.utils.LoggerInfo;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupsController {
    private static final Logger LOGGER = Logger.getLogger(GroupsController.class);
    private final GroupsService service;
    private final UserService userService;
    private final Gson gson;

    public GroupsController(GroupsService service, UserService userService, Gson gson) {
        this.service = service;
        this.userService = userService;
        this.gson = gson;
    }

    @GetMapping("/groups")
    public ResponseEntity<?> getAllGroups(){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "getAllGroups", "/groups", null, null, startDate));

        List<GroupModel> list = new ArrayList<>();
        CommonResponseModel res = null;
        try{
            list = service.getAllGroups();
            if (list == null || list.isEmpty()){
                LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getAllGroups", "/groups",  null, null, startDate, new Date(), Consts.NO_USER_FOUND_IN_DATABASE));
                res = new CommonResponseModel(HttpStatus.NO_CONTENT.value(), Consts.NO_USER_FOUND_IN_DATABASE);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
            }
        }catch(Exception e){
            LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getAllGroups", "/groups",  null, null, startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getAllGroups", "/groups",  null, null, startDate, new Date(), gson.toJson(list)));
        String jData = AESUtil.encrypt(gson.toJson(list), currentUser==null?null:currentUser.getSecretCode());
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), jData );
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @GetMapping("/groups/{id}")
    public ResponseEntity<?> getGroupsById(@PathVariable long id){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "getGroupsById", "/groups/" + id, null, String.valueOf(id), startDate));

        GroupModel resGroups = null;
        CommonResponseModel res = null;
        try {
            resGroups = service.getGroupsById(id);
        } catch (BadRequestException e) {
            LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getGroupsById", "/groups/"+ id,  null, String.valueOf(id), startDate, new Date(), e.getMessage() + id));
            res = new CommonResponseModel(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getGroupsById", "/groups/"+ id,  null, String.valueOf(id), startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }

        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "getGroupsById", "/groups/"+ id,  null, String.valueOf(id), startDate, new Date(),resGroups.toString()));
        String jData = AESUtil.encrypt(String.valueOf(resGroups), currentUser==null?null:currentUser.getSecretCode());
        res = new CommonResponseModel(String.valueOf(startDate), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), jData);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PostMapping("/groups")
    public ResponseEntity<?> createGroups(@RequestBody GroupModel model){
        Date startDate = new Date();
        User currentUser =  userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "createGroups", "/groups" , null, model.toString(), startDate));
        GroupModel resGroup = null;
        CommonResponseModel res = null;
        try{
            resGroup = service.saveGroups(model);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "createGroups", "/groups",  null, model.toString(), startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "createGroups", "/groups",  null, model.toString(), startDate, new Date(), resGroup.toString()));
        String jData = AESUtil.encrypt(gson.toJson(resGroup), currentUser==null?null:currentUser.getSecretCode());
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), resGroup.toString());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PutMapping("/groups")
    public ResponseEntity<?> updateGroups(@RequestBody GroupModel model){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "updateGroups", "/groups", null, null, startDate));
        GroupModel resGroups = null;
        CommonResponseModel res = null;
        try{
            resGroups = service.saveGroups(model);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "updateGroups", "/groups",  null, model.toString(), startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "updateGroups", "/groups",  null, model.toString(), startDate, new Date(), resGroups.toString()));
        String jData = AESUtil.encrypt(gson.toJson(resGroups), currentUser==null?null:currentUser.getSecretCode());
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), jData);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable long id){
        Date startDate = new Date();
        User currentUser = userService.getCurrentUserLogin();
        LOGGER.info(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), "deletePermission", "/permissions", null, null, startDate));
        CommonResponseModel res = null;
        try{
            service.deleteGroups(id);
        }catch (Exception e){
            LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "deleteGroups", "/groups",  null, String.valueOf(id), startDate, new Date(), e.getMessage()));
            res = new CommonResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
        LOGGER.error(new LoggerInfo(currentUser==null?null:currentUser.getUsername(), currentUser==null?null:currentUser.getAuthorities().stream().map(role -> role.toString()).toString(), "deleteGroups", "/groups",  null, String.valueOf(id), startDate, new Date(), Consts.DELETE_GROUPS_SUCCESSFULLY + id));
        res = new CommonResponseModel(startDate.toString(), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
