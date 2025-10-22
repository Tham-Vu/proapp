package com.example.user_management.service.serviceimpl;

import com.example.user_management.entity.Groups;
import com.example.user_management.entity.User;
import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.mapper.UserMapper;
import com.example.user_management.model.request.LoginModel;
import com.example.user_management.model.request.UserModel;
import com.example.user_management.model.response.TokenResponse;
import com.example.user_management.repo.GroupsRepo;
import com.example.user_management.repo.UserRepo;
import com.example.user_management.service.UserService;
import com.example.user_management.utils.Consts;
import com.example.user_management.utils.JWTUtils;
import com.example.user_management.utils.LoggerInfo;
import com.example.user_management.utils.SqlStatementInspector;
import jakarta.ws.rs.InternalServerErrorException;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class);
    private final UserRepo userRepo;
    private final GroupsRepo groupsRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, GroupsRepo groupsRepo, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.groupsRepo = groupsRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserModel getUserById(long id) throws BadRequestException {
        User user = userRepo.findById(id).orElseThrow(()-> new BadRequestException(Consts.USER_NOT_FOUND));
        return userMapper.toDto(user);
    }
    @Override
    public User getCurrentUserLogin(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        User currentUser = new User();
        if (authentication != null){
            Object principal = authentication.getPrincipal();
            if (principal instanceof String) {
                return null;
            }
            if (principal instanceof User) {
                return (User) principal;
            }
            currentUser = (User) authentication.getPrincipal();
        }
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

    @Override
    public UserModel saveUser(UserModel userModel) throws BadRequestException {
        User savedUser = new User();
        if (userModel.getId() == null){
            //create new User
            User newUser = userMapper.toEntity(userModel);
            Groups groups = groupsRepo.findById(userModel.getGroupsId()).orElseThrow(()->{
                LOGGER.warn(new LoggerInfo("savedUser", new Date(), Consts.GROUPS_NOT_FOUND_IN_DATABASE + userModel.getGroupsId()));
                return new BadRequestException(Consts.GROUPS_NOT_FOUND_IN_DATABASE + userModel.getGroupsId());
            });
            newUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
            newUser.setGroups(groups);
            savedUser = userRepo.save(newUser);
        }else {
            //update old User
            User existedUser = userRepo.findById(userModel.getId()).orElseThrow(()->{
                LOGGER.warn(new LoggerInfo("savedUser", new Date(), Consts.USER_NOT_FOUND_IN_DATABASE + userModel.getId()));
                return new BadRequestException(Consts.USER_NOT_FOUND_IN_DATABASE + userModel.getId());
            });
            existedUser.setUsername(userModel.getUsername());
            existedUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
            existedUser.setFirstName(userModel.getFirstName());
            existedUser.setLastName(userModel.getLastName());
            existedUser.setPhoneNumber(userModel.getPhoneNumber());
            existedUser.setDateOfBirth(userModel.getDateOfBirth());
            existedUser.setEmail(userModel.getEmail());
            existedUser.setPublicKey(userModel.getPublicKey());
            existedUser.setSecretCode(userModel.getSecretCode());
            existedUser.setCreateDate(userModel.getCreateDate());
            existedUser.setUpdateDate(userModel.getUpdateDate());
            existedUser.setActive(userModel.isActive());
            Groups groups = groupsRepo.findById(userModel.getGroupsId()).orElseThrow(()->{
                LOGGER.warn(new LoggerInfo("savedUser", new Date(), Consts.GROUPS_NOT_FOUND_IN_DATABASE + userModel.getGroupsId()));
                return new BadRequestException(Consts.GROUPS_NOT_FOUND_IN_DATABASE + userModel.getGroupsId());
            });
            existedUser.setGroups(groups);
            //redefine business logic -update user
            savedUser = userRepo.save(existedUser);
        }
        if (savedUser==null){
            LOGGER.warn(new LoggerInfo("savedUser", new Date(), Consts.ERROR_UPDATE_DATABASE ));
            throw new InternalServerErrorException(Consts.ERROR_UPDATE_DATABASE + SqlStatementInspector.getLastSql());
        }
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserModel deleteUser(Long id) throws BadRequestException {
        User existedUser = userRepo.findById(id).orElseThrow(()->{
            LOGGER.warn(new LoggerInfo("deleteUser", new Date(), Consts.USER_NOT_FOUND_IN_DATABASE + id));
            return new BadRequestException(Consts.USER_NOT_FOUND_IN_DATABASE + id);
        });
        userRepo.deleteById(id);
        userRepo.flush();
        return null;
    }

    @Override
    public UserModel changeActive(Long id) throws BadRequestException {
        User savedUser = null;
        User existedUser = userRepo.findById(id).orElseThrow(()-> {
            LOGGER.warn(new LoggerInfo("changeActive", new Date(), Consts.USER_NOT_FOUND_IN_DATABASE + id));
            return new BadRequestException(Consts.USER_NOT_FOUND_IN_DATABASE + id);
        });
        boolean isActive = existedUser.isActive();
        if (isActive) {
            existedUser.setActive(false);
        } else existedUser.setActive(true);
        savedUser = userRepo.save(existedUser);
        if (savedUser==null){
            LOGGER.warn(new LoggerInfo("changeActive", new Date(), Consts.ERROR_UPDATE_DATABASE ));
            throw new InternalServerErrorException(Consts.ERROR_UPDATE_DATABASE + SqlStatementInspector.getLastSql());
        }
        return userMapper.toDto(savedUser);
    }
}
