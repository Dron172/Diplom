package com.example.CloudStorage.security;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    @Value("${cloud.app.jwtSecret}")
    private String jwtSecret;

    @Value("${cloud.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(UserDetails userPrincipal) {
        //UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String authToken) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody().getSubject();
    }

    //TODO:добить ошибки или объединить

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
        } catch (MalformedJwtException e) {
        } catch (ExpiredJwtException e) {
        } catch (UnsupportedJwtException e) {
        } catch (IllegalArgumentException e) {
        }

        return false;
    }

    private Boolean isTokenExpired(String authToken) {
        final Date expiration = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody().getExpiration();
        return expiration.before(new Date());
    }
}
