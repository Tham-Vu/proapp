package com.example.user_management.utils;

import com.example.user_management.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtils implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(JWTUtils.class);
    private String jwtSecret = Consts.JWT_SECRET;
    private long jwtExpire = Consts.JWT_EXPIRE;
    private long jwtRefreshExpire = Consts.JWT_REFRESH_EXPIRE;
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }
    public Boolean validateToken(String token, User user, HttpServletRequest request){
        try{
            final String username = getUsernameFromToken(token);
            return (username.equals(user.getUsername()) && !isTokenExpired(token));
        }catch (ExpiredJwtException e){
            request.setAttribute("expired", e.getMessage());
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), Consts.TOKEN_EXPIRE_EXCEPTION_MESS);
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user){
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpire))
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }
    public String generateRefreshToken(User user){
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpire))
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }
}
