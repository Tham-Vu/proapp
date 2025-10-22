package com.example.user_management.service;

import com.example.user_management.entity.User;
import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.request.LoginModel;
import com.example.user_management.model.request.UserModel;
import com.example.user_management.model.response.TokenResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService{
    UserModel getUserById(long id) throws BadRequestException;

    User getCurrentUserLogin();

    List<UserModel> getAllUser();

    UserModel saveUser(UserModel userModel) throws BadRequestException;

    UserModel deleteUser(Long id) throws BadRequestException;

    UserModel changeActive(Long id) throws BadRequestException;

}
