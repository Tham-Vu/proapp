package com.example.proapp_gateway.utils.jwt;

import com.example.proapp_gateway.utils.Constants;
import com.example.proapp_gateway.utils.LoggerInfo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@Component
public class JwtFilter implements Filter {
    public static final Logger LOGGER = Logger.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Date startDate = new Date();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authHeader = request.getHeader(Constants.AUTHORIZATION);
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/refresh-token")){
            filterChain.doFilter(request, servletResponse);
        }else {
            if (authHeader != null && authHeader.startsWith(Constants.BEARER)) {
                String token = authHeader.substring(7);
                String username = null;
                String role = null;
                try {
                    username = jwtUtil.getClaimFromToken(token, Claims::getSubject);
                    role = jwtUtil.getClaimFromToken(token, claims -> claims.get(Constants.ROLE)).toString();
                    request.setAttribute(Constants.USERNAME, username);
                    request.setAttribute(Constants.ROLE, role);
                    filterChain.doFilter(request, servletResponse);
                } catch (Exception e) {
                    LoggerInfo loggerInfo = new LoggerInfo(username, role, JwtFilter.class.getName(), request.getRequestURI(), null, request.getQueryString(), startDate, new Date(), e.getMessage());
                    LOGGER.error(loggerInfo);
                    throw new ServletException(Constants.INVALID_JWT_TOKEN);
                }
            } else {
                LoggerInfo loggerInfo = new LoggerInfo(null, null, JwtFilter.class.getName(), request.getRequestURI(), null, request.getQueryString(), startDate, new Date(), Constants.MISSING_AUTH_HEADER);
                LOGGER.error(loggerInfo);
                throw new ServletException(Constants.MISSING_AUTH_HEADER);
            }
        }
    }
}
