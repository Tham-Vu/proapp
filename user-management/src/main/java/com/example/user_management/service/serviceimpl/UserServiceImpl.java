package com.example.user_management.service.serviceimpl;

import com.example.user_management.entity.User;
import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.mapper.UserMapper;
import com.example.user_management.model.request.UserModel;
import com.example.user_management.repo.UserRepo;
import com.example.user_management.service.UserService;
import com.example.user_management.utils.Consts;
import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class);
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepo userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    @Override
    public UserModel getUserById(long id) throws BadRequestException {
        User user = userRepo.findById(id).orElseThrow(()-> new BadRequestException(Consts.USER_NOT_FOUND));
        return userMapper.toDto(user);
    }
    public UserModel getUserLogin(){

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        return null;
    }
}
