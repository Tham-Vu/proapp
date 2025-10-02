package com.example.proapp_gateway.utils.jwt;

import com.example.proapp_gateway.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.function.Function;

public class JwtUtil {
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }
    public Boolean validateToken(String token, HttpServletRequest request){
        try{
            final String username = getUsernameFromToken(token);
            return !isTokenExpired(token);
        }catch (ExpiredJwtException e){
            request.setAttribute("expired", e.getMessage());
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), Constants.TOKEN_EXPIRE_EXCEPTION_MESS);
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(Constants.Config.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
