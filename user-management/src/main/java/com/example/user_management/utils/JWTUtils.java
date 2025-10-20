package com.example.user_management.utils;

import com.example.user_management.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class JWTUtils implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(JWTUtils.class);

    public String generateToken(User user){
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Consts.JWT_EXPIRE))
                .setSubject(user.getUsername())
                .claim("role", user.getGroups()!=null?user.getGroups().getName():null)
                .signWith(SignatureAlgorithm.HS256, Consts.JWT_SECRET)
                .compact();
    }
    public String generateRefreshToken(User user){
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Consts.JWT_REFRESH_EXPIRE))
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS256, Consts.JWT_SECRET)
                .compact();
    }
}
