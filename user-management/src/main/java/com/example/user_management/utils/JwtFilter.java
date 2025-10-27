package com.example.user_management.utils;

import com.example.user_management.entity.User;
import com.example.user_management.service.serviceimpl.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
@Component
public class JwtFilter extends OncePerRequestFilter {
    public static final Logger LOGGER = Logger.getLogger(JwtFilter.class);
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
        if(path.equals("/api/login") || path.equals("/api/refresh-token") || path.equals("/api/logout")){
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader(Consts.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(Consts.BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        try {
            if(!jwtUtils.isTokenExpired(token)) {
                String username = (String) request.getAttribute(Consts.USERNAME);
                String role = (String) request.getAttribute(Consts.ROLE);
                if (username == null) {
                    username = jwtUtils.getClaimFromToken(token, Claims::getSubject);
                }
                if (role == null) {
                    role = jwtUtils.getClaimFromToken(token, claims -> claims.get(Consts.ROLE, String.class));
                }
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = (User) service.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }catch (ExpiredJwtException e) {
            LOGGER.error("Token expired!");
            throw new RuntimeException(e.getMessage());
        } catch (SignatureException e) {
            LOGGER.warn("Invalid token signature");
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unexpected JWT parsing error", e);
            throw new RuntimeException(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
