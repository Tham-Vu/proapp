package com.example.user_management.utils;

import jakarta.annotation.PostConstruct;
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
    public static final String NO_CONTENT = "No content";
    public static final String NO_USER_FOUND_IN_DATABASE = "No users found in the database";
    public static final String NO_PERMISSION_FOUND_IN_DATABASE = "No permission found in the database";
    public static final String NO_GROUPS_FOUND_IN_DATABASE = "No groups found in the database";
    public static final String USER_NOT_FOUND_IN_DATABASE = "User not found in database ";
    public static final String PERMISSION_NOT_FOUND_IN_DATABASE = "Permission not found in database ";
    public static final String GROUPS_NOT_FOUND_IN_DATABASE = "Groups not found in database ";
    public static final String ERROR_UPDATE_DATABASE = "An error occurred while updating the database.";
    public static final String SQL_NOT_CAPTURED = "Sql not captured ";
    public static final String DELETE_USER_SUCCESSFULLY = "Delete user successfully ";
    public static final String DELETE_PERMISSION_SUCCESSFULLY = "Delete permission successfully ";
    public static final String DELETE_GROUPS_SUCCESSFULLY = "Delete permission successfully ";
    public static Charset SYSTEM_CHARSET = Charset.forName("UTF-8");
    public static String USER_NOT_FOUND = "User id is not found ";

    public static String JWT_SECRET;
    public static long JWT_EXPIRE;
    public static long JWT_REFRESH_EXPIRE;
    public static String TOKEN_EXPIRE_EXCEPTION_MESS;
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpire;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long jwtRefreshExpire;

    @Value("${app.token.expired}")
    private String tokenExpireExceptionMess;

    @PostConstruct
    public void init(){
        JWT_SECRET = this.jwtSecret;
        JWT_EXPIRE = this.jwtExpire;
        JWT_REFRESH_EXPIRE = this.jwtRefreshExpire;
        TOKEN_EXPIRE_EXCEPTION_MESS = this.tokenExpireExceptionMess;
    }
}
