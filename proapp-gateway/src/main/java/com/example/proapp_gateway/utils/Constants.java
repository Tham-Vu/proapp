package com.example.proapp_gateway.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class Constants {
    public static final String RSA_INSTANCE = "SHA256withRSA";
    public static Charset SYSTEM_CHARSET = Charset.forName("UTF-8");

    public static class Config{

        @Value("${app.jwt.secret}")
        public static String JWT_SECRET;
        @Value("${app.jwt.expiration-ms}")
        public static String JWT_EXPIRE;
        @Value("${app.jwt.expiration-refresh-ms}")
        public static String JWT_REFRESH_EXPIRE;
    }
    public static final String USERNAME = "username";
    public static final String ROLE = "role";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String TOKEN_EXPIRE_EXCEPTION_MESS = "JWT token has expired!";
    public static final String INVALID_JWT_TOKEN = "Invalid JWT token ";
    public static final String MISSING_AUTH_HEADER = "Missing authorization header";
}
