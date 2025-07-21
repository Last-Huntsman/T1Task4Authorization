package org.zuzukov.t1task4.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zuzukov.t1task4.dto.JwtAuthenticationDto;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Value("${jwt.secret}")
    private String jwtSecret;

    public JwtAuthenticationDto generateJwtAuthenticationDto(String email) {
        JwtAuthenticationDto jwtAuthenticationDto = new JwtAuthenticationDto();
        jwtAuthenticationDto.setToken(generateJwtToken(email));
        jwtAuthenticationDto.setRefreshToken(generateRefreshJwtToken(email));
    return jwtAuthenticationDto;}

    public JwtAuthenticationDto refreshBaseToken(String email, String refreshToken) {
        if (!validateRefreshToken(refreshToken, email)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        JwtAuthenticationDto jwtAuthenticationDto = new JwtAuthenticationDto();
        jwtAuthenticationDto.setToken(generateJwtToken(email));
        jwtAuthenticationDto.setRefreshToken(refreshToken);
        return jwtAuthenticationDto;
    }

    public String generateJwtToken(String email) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(email).
                expiration(date).
                signWith(getSecretKey())
                .compact();
    }
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
    public String generateRefreshJwtToken(String email) {
        Date date = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(email).
                expiration(date).
                signWith(getSecretKey())
                .compact();
    }
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getBody();
            return true;

        }
        catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
        }
        catch (UnsupportedJwtException e){
            log.info("Unsupported JWT token");
        }
        catch (MalformedJwtException e){
            log.info("Malformed JWT token");
        }
        catch (SecurityException e){
            log.info("Security exception");
        }
        catch (Exception e){
            log.info("Invalid token");
        }
        return false;
    }
    public SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public boolean validateRefreshToken(String refreshToken, String email) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();

            String subject = claims.getSubject();
            Date expiration = claims.getExpiration();
            if (!subject.equals(email)) {
                log.info("Email in token does not match");
                return false;
            }
            if (expiration.before(new Date())) {
                log.info("Refresh token is expired");
                return false;
            }
            return true;

        } catch (Exception e) {
            log.info("Invalid refresh token: {}", e.getMessage());
            return false;
        }
    }


}
