package com.example.userservice.domain.auth.jwt;

import com.example.userservice.domain.Member.dto.request.MemberLoginRequestDto;
import com.example.userservice.domain.Member.dto.response.MemberLoginResponseDto;
import com.example.userservice.domain.auth.cookie.CookieUtil;
import com.example.userservice.domain.auth.service.RefreshTokenService;
import com.example.userservice.global.exception.error.NotFoundAccountException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.refreshTokenService = refreshTokenService;
        this.jwtProvider = jwtProvider;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("JwtAuthenticationFilter.attemptAuthentication");

        ObjectMapper objectMapper = new ObjectMapper();
        MemberLoginRequestDto memberLoginRequestDto = null;
        try {
            memberLoginRequestDto = objectMapper.readValue(request.getInputStream(), MemberLoginRequestDto.class);
        } catch (Exception e) {
            // no login request dto
            log.info("no login request dto");
            throw new NotFoundAccountException("계정이 존재하지 않습니다.");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(memberLoginRequestDto.getUsername(), memberLoginRequestDto.getPassword());
        Authentication authentication = null;
        authentication = this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("JwtAuthenticationFilter.successfulAuthentication");

        MemberDetails memberDetails = (MemberDetails) authResult.getPrincipal();

        String accessToken = jwtProvider.generateAccessToken(memberDetails.getUsername());
        String refreshToken = jwtProvider.generateRefreshToken(memberDetails.getUsername());

        refreshTokenService.setRefreshToken(memberDetails.getUsername(), refreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        CookieUtil.addCookie(response, "accessToken", accessToken, jwtProvider.ACCESS_TOKEN_EXPIRATION_TIME);
        CookieUtil.addCookie(response, "refreshToken", refreshToken, jwtProvider.REFRESH_TOKEN_EXPIRATION_TIME);

        // token body comment
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        MemberLoginResponseDto.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .build()
                )
        );
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("JwtAuthenticationFilter.unsuccessfulAuthentication");
        throw new NotFoundAccountException("계정이 존재하지 않습니다.");
    }
}
