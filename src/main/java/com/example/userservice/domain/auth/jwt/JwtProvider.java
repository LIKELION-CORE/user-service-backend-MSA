package com.example.userservice.domain.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${token.secret}")
    private String SECRET_KEY;

    public final long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 30; // 30 min
    public final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14; // 14 week

    private final String HEADER_NAME = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public String generateAccessToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("userId", username)
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }
    public String generateRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .withClaim("userId", username)
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public boolean verifyToken(String token) {
        log.info("SECRET_KEY = " + SECRET_KEY);
        try {
            JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getHeader(HttpServletRequest request) {
        String header = request.getHeader(HEADER_NAME);
        if (header != null && !header.startsWith(TOKEN_PREFIX)) header = null;
        return header;
    }

    public String getUserId(HttpServletRequest request) {
        String accessToken = getHeader(request).replace(TOKEN_PREFIX, "");
        System.out.println("test=>"+accessToken);
        return JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(accessToken)
                .getClaim("userId").asString();
    }
    public String getUsernameFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(token)
                .getClaim("userId").asString();
    }
}