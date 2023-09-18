package com.example.userservice.global.security;


import com.example.userservice.domain.Member.service.MemberService;
import com.example.userservice.domain.auth.jwt.JwtAuthenticationFilter;
import com.example.userservice.domain.auth.jwt.JwtAuthorizationFilter;
import com.example.userservice.domain.auth.jwt.JwtProvider;
import com.example.userservice.domain.auth.service.RefreshTokenService;
import com.example.userservice.global.exception.GlobalExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.BindException;
import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final GlobalExceptionHandlerFilter globalExceptionHandlerFilter;
    @Value("${permitUrls.all}")
    private String[] permitUrls_all;

    @Value("${permitUrls.get}")
    private String[] permitUrls_get;

    @Value("${permitUrls.post}")
    private String[] permitUrls_post;

    @Value("${permitUrls.put}")
    private String[] permitUrls_put;

    @Value("${permitUrls.patch}")
    private String[] permitUrls_patch;

    @Value("${permitUrls.delete}")
    private String[] permitUrls_delete;


    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        Map<HttpMethod, String[]> permitUrls = new HashMap<>();
        permitUrls.put(HttpMethod.GET, ArrayUtils.addAll(permitUrls_all, permitUrls_get));
        permitUrls.put(HttpMethod.POST, ArrayUtils.addAll(permitUrls_all, permitUrls_post));
        permitUrls.put(HttpMethod.DELETE, ArrayUtils.addAll(permitUrls_all, permitUrls_delete));
        permitUrls.put(HttpMethod.PUT, ArrayUtils.addAll(permitUrls_all, permitUrls_put));
        permitUrls.put(HttpMethod.PATCH, ArrayUtils.addAll(permitUrls_all, permitUrls_patch));
        httpSecurity
                .exceptionHandling()
                .and()
                .formLogin().disable() // 초기 로그인화면 자동생성 안함
                .csrf().disable() // rest api 에서 csrf 방어 필요 없음
                .cors().configurationSource(corsConfigurationSource()).and() // cors
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()// 세션 생성 안함 , response 에 setcookie jsessionId= ... 안함
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), refreshTokenService, jwtProvider))
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), memberService, jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login","/join","/info").permitAll()
                .antMatchers(HttpMethod.GET, "/health_check").permitAll()
                .anyRequest().authenticated();

        httpSecurity.addFilterAfter(globalExceptionHandlerFilter,LogoutFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*localhost*"); // A list of origins for which cross-origin requests are allowed. ex) http://localhost:8080
        configuration.addAllowedHeader("*"); // Set the HTTP methods to allow ,ex) "GET", "POST", "PUT";
        configuration.addAllowedMethod("*"); // Set the list of headers that a pre-flight request can list as allowed for use during an actual request. ex) "Authorization", "Cache-Control", "Content-Type"
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public void configure(WebSecurity webSecurity){
        webSecurity.ignoring().antMatchers("/v2/api-docs"
                , "/swagger-resources/**"
                , "/swagger-ui.html"
                , "/webjars/**"
                , "/swagger/**"
                , "/verify/**"
        ).antMatchers(HttpMethod.POST, "/user");
    }

}