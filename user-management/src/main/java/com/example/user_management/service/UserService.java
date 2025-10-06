package com.example.user_management.service;

import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.request.UserModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserModel getUserById(long id) throws BadRequestException;
}
