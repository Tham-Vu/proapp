package com.example.user_management.utils;

import com.example.user_management.entity.User;
import com.example.user_management.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final UserService service;

    public JwtFilter(JWTUtils jwtUtils, UserService service) {
        this.jwtUtils = jwtUtils;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Date date = new Date();
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/refresh-token")){
            filterChain.doFilter(request, response);
        }else {
            String username = null;
            String password = null;
            String authHeader = request.getHeader(Consts.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith(Consts.BEARER)) {
                String token = authHeader.substring(7);
                try {
                    username = String.valueOf(request.getAttribute(Consts.USERNAME));
                    password = String.valueOf(request.getAttribute(Consts.ROLE));
//                    User user = service.loadUserByUsername(username);
                }catch (Exception e){
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }
}
