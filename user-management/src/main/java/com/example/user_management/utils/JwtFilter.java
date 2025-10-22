package com.example.user_management.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final UserDetailsService service;

    public JwtFilter(JWTUtils jwtUtils, UserDetailsService service) {
        this.jwtUtils = jwtUtils;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Date date = new Date();
        String path = request.getServletPath().trim();
        if(path.equals("/api/login") || path.equals("/api/refresh-token") || path.startsWith("/api")){
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
//                    filterChain.doFilter(request, response);
                }catch (Exception e){
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }
}
