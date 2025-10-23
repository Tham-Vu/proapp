package com.example.user_management.service.serviceimpl;

import com.example.user_management.entity.User;
import com.example.user_management.repo.UserRepo;
import com.example.user_management.utils.Consts;
import com.example.user_management.utils.LoggerInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOGGER = Logger.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null){
            LOGGER.warn(new LoggerInfo("loadUserByUsername", new Date(), Consts.USER_NOT_FOUND_IN_DATABASE + username));
            throw new UsernameNotFoundException(Consts.USER_NOT_FOUND_IN_DATABASE + username);
        }
        return user;
    }
}
