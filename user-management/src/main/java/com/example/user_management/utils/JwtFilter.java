package com.example.user_management.utils;

import com.example.user_management.entity.User;
import com.example.user_management.service.serviceimpl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final UserDetailsServiceImpl service;

    public JwtFilter(JWTUtils jwtUtils, UserDetailsServiceImpl service) {
        this.jwtUtils = jwtUtils;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Date date = new Date();
        String path = request.getServletPath().trim();
        if(path.equals("/api/login") || path.equals("/api/refresh-token") || path.equals("/api/logout") || path.startsWith("/api")){
            filterChain.doFilter(request, response);
        }else {
            String username = null;
            String role = null;
            String authHeader = request.getHeader(Consts.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith(Consts.BEARER)) {
                String token = authHeader.substring(7);
                try {
                    username = String.valueOf(request.getAttribute(Consts.USERNAME));
                    role = String.valueOf(request.getAttribute(Consts.ROLE));
                    User user = (User) service.loadUserByUsername(username);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                }catch (Exception e){
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }
}
