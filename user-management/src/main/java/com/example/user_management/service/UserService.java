package com.example.user_management.service;

import com.example.user_management.entity.User;
import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.request.UserModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserModel getUserById(long id) throws BadRequestException;

    User getCurrentUserLogin();

    List<UserModel> getAllUser();
}
