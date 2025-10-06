package com.example.user_management.controller;

import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.request.UserModel;
import com.example.user_management.service.UserService;
import com.example.user_management.utils.LoggerInfo;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    public static final Logger LOGGER = Logger.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id){
        UserModel userModel = null;
        try {
            userModel = userService.getUserById(id);
        } catch (BadRequestException e) {
            LOGGER.error(new LoggerInfo());
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userModel);
    }

}
