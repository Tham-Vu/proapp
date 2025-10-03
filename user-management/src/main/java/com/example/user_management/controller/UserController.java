package com.example.user_management.controller;

import com.example.user_management.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    public static final Logger LOGGER = Logger.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

}
