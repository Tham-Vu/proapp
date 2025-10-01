package com.example.user_management.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Consts {
    @Value("${app.jwt.secret}")
    public static String JWT_SECRET;
    @Value("${app.jwt.expiration-ms}")
    public static long JWT_EXPIRE;
    @Value("${app.jwt.refresh-expiration-ms}")
    public static long JWT_REFRESH_EXPIRE;
    @Value("${app.token.expired}")
    public static String TOKEN_EXPIRE_EXCEPTION_MESS;
}
