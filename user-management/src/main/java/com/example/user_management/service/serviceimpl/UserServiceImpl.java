package com.example.user_management.service.serviceimpl;

import com.example.user_management.entity.User;
import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.mapper.UserMapper;
import com.example.user_management.model.request.UserModel;
import com.example.user_management.repo.UserRepo;
import com.example.user_management.service.UserService;
import com.example.user_management.utils.Consts;
import com.example.user_management.utils.LoggerInfo;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        return null;
    }
    @Override
    public User getCurrentUserLogin(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser;
    }

    @Override
    public List<UserModel> getAllUser() {
        List<User> listUser = userRepo.findAll();
        if (listUser == null || listUser.isEmpty()){
            LOGGER.warn(new LoggerInfo("getAllUser", new Date(), Consts.NO_CONTENT));
            return Collections.emptyList();
        }
        return userMapper.toDto(listUser);
    }
}
