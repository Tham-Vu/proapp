package com.example.proapp_gateway.utils.jwt;

import com.example.proapp_gateway.utils.Constants;
import com.example.proapp_gateway.utils.JsonResponse;
import com.example.proapp_gateway.utils.LoggerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.apache.log4j.Logger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
public class JwtFilter implements WebFilter {
    public static final Logger LOGGER = Logger.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    public JwtFilter(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Date startDate = new Date();
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        ServerHttpRequest request = exchange.getRequest();
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        String path = request.getPath().value();
        if (path.startsWith("/user/api/login")
                || path.startsWith("/user/api/refresh-token")
                || path.startsWith("/user/")) {
            return chain.filter(exchange);
        }
        else {
            if (authHeader != null && authHeader.startsWith(Constants.BEARER)) {
                String token = authHeader.substring(7);
                String username = null;
                String role = null;
                try {
                    username = jwtUtil.getClaimFromToken(token, Claims::getSubject);
                    role = jwtUtil.getClaimFromToken(token, claims -> claims.get(Constants.ROLE)).toString();
                    exchange.getAttributes().put(Constants.USERNAME, username);
                    exchange.getAttributes().put(Constants.ROLE, role);
                    return chain.filter(exchange);
                } catch (Exception e) {
                    LoggerInfo loggerInfo = new LoggerInfo(username, role, JwtFilter.class.getName(), request.getPath().value(), null, request.getBody().toString(), startDate, new Date(), Constants.INVALID_JWT_TOKEN + e.getMessage());
                    LOGGER.error(loggerInfo);
//                    return exchange.getResponse().setComplete();
                    return writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED, Constants.INVALID_JWT_TOKEN, bufferFactory, objectMapper);
                }
            } else {
                LoggerInfo loggerInfo = new LoggerInfo(JwtFilter.class.getName(), request.getPath().value(), request.getBody().toString(), startDate, new Date(), Constants.MISSING_AUTH_HEADER);
                LOGGER.error(loggerInfo);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED, Constants.MISSING_AUTH_HEADER, bufferFactory, objectMapper);
            }
        }
    }
    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, HttpStatus status, String message, DataBufferFactory bufferFactory, ObjectMapper objectMapper) {

        // 1. Chuẩn bị Response
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        JsonResponse res = new JsonResponse(status.value(), message);
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(res);
            DataBuffer buffer = bufferFactory.wrap(bytes);
            //Ghi Response Body và hoàn thành Exchange
            return exchange.getResponse().writeWith(Mono.just(buffer));

        } catch (Exception e) {
            LOGGER.error(Constants.FAILED_WRITE_RESPONSE_BODY, e);
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }
}
