package com.example.user_management.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class Consts {
    public static final String AES_INSTANCE = "AES/ECB/PKCS5Padding";
    public static final String RSA_INSTANCE = "SHA256withRSA";
    public static final String AES = "AES";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String USERNAME = "username";
    public static final String ROLE = "role";
    public static Charset SYSTEM_CHARSET = Charset.forName("UTF-8");
    @Value("${app.jwt.secret}")
    public static String JWT_SECRET;
    @Value("${app.jwt.expiration-ms}")
    public static long JWT_EXPIRE;
    @Value("${app.jwt.refresh-expiration-ms}")
    public static long JWT_REFRESH_EXPIRE;
    @Value("${app.token.expired}")
    public static String TOKEN_EXPIRE_EXCEPTION_MESS;
    public static String USER_NOT_FOUND = "User id is not found";
}
