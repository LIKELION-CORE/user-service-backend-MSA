package com.example.userservice.domain.Member.controller;


import com.example.userservice.domain.Member.dto.request.*;
import com.example.userservice.domain.Member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.Member.dto.response.MemberInfoResponseDto;
import com.example.userservice.domain.Member.dto.response.MemberRenewAccessTokenResponseDto;
import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.Member.service.MemberService;
import com.example.userservice.domain.auth.cookie.CookieUtil;
import com.example.userservice.domain.auth.jwt.JwtProvider;
import com.example.userservice.domain.auth.jwt.MemberDetails;
import com.example.userservice.domain.auth.service.RefreshTokenService;
import com.example.userservice.global.common.CommonResDto;
import com.example.userservice.global.exception.error.InvalidTokenException;
import com.example.userservice.global.exception.error.NotFoundAccountException;
import com.example.userservice.global.exception.error.UnAuthorizedException;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/member")
public class MemberController {
    private final Environment env;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/renew-access-token")
    public ResponseEntity renewAccessToken(
                                           HttpServletResponse response,
                                           HttpServletRequest request
                                           ) {

        String cookieRefreshToken = CookieUtil.getRefreshTokenCookie(request);
        log.info("before renew refreshToken : "+cookieRefreshToken);
        try{

            Authentication authentication = jwtProvider.getAuthentication(cookieRefreshToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (authentication == null || !authentication.isAuthenticated()) {
                throw new AuthenticationCredentialsNotFoundException("User not authenticated");
            }

            jwtProvider.verifyToken(cookieRefreshToken);

            String accessToken = memberService.renewAccessToken(cookieRefreshToken,authentication);
            String refreshToken = jwtProvider.generateRefreshToken(authentication.getName(), authentication);

            refreshTokenService.setRefreshToken(authentication.getName(),refreshToken);
            CookieUtil.addCookie(response,"refreshToken", refreshToken,jwtProvider.REFRESH_TOKEN_EXPIRATION_TIME);

            // token body comment
            return ResponseEntity.ok(MemberRenewAccessTokenResponseDto.builder()
                    .accessToken(accessToken)
                    .build());
        }catch (InvalidTokenException invalidTokenException){
            throw new InvalidTokenException("토큰이 유효하지않습니다");
        }
    }

    @PostMapping("")
    public ResponseEntity<CommonResDto<CreateMemberResponseDto>> createMember(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {

        log.info("회원가입 진행 중");
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(signUpRequestDto));
    }
    @GetMapping("")
    public ResponseEntity<MemberInfoResponseDto> readMemberInfo(Principal principal) {
        return new ResponseEntity<>(
                memberService.getMemberInfo(principal.getName()), HttpStatus.OK
        );
    }
    @PutMapping("")
    public ResponseEntity<?> updateMember(Principal principal,
                                          @Valid @RequestBody UpdateMemberRequesstDto updateMemberRequesstDto) {

        log.info("회원수정 진행 중");
        if(principal==null){
            throw new NotFoundAccountException("유저를 찾을 수 없습니다");
        }

        memberService.updateMember(principal.getName(),updateMemberRequesstDto);
        return new ResponseEntity<>(new CommonResDto<>(1,"회원수정완료",""), HttpStatus.OK);
    }

    @PutMapping("/admin/update")
    public ResponseEntity<?> updateMemberByAdmin(Principal principal,
                                          HttpServletRequest request,
                                          @Valid @RequestBody UpdateMemberByAdminRequestDto updateMemberByAdminRequestDto) {

        log.info("관리자 회원수정 진행 중");
        if(principal==null){
            throw new NotFoundAccountException("유저를 찾을 수 없습니다");
        }
        String refreshTokenCookie = CookieUtil.getRefreshTokenCookie(request);
        List<String> rolesFromToken = jwtProvider.getRolesFromToken(refreshTokenCookie);

        if(rolesFromToken.contains("ROOT") || rolesFromToken.contains("ADMIN")){

            memberService.updateMemberByAdmin(principal.getName(),updateMemberByAdminRequestDto);
        }else{
            throw new UnAuthorizedException("권한이 없습니다");
        }
        return new ResponseEntity<>(new CommonResDto<>(1,"회원수정완료",""), HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<?> updateMemberPassword(Principal principal,
                                                 @Valid @RequestBody UpdateMemberPasswordRequestDto updateMemberPasswordRequestDto) {
        if(principal==null){
            throw new NotFoundAccountException("유저를 찾을 수 없습니다");
        }
        memberService.memberPasswordUpdate(principal.getName(),updateMemberPasswordRequestDto);
        return new ResponseEntity<>(new CommonResDto<>(1,"패스워드수정완료",""), HttpStatus.OK);
    }



    @DeleteMapping("")
    public ResponseEntity<?> deleteMember(Principal principal) {

        log.info("회원삭제 진행 중");
        return new ResponseEntity<>(new CommonResDto<>(1,"회원삭제완료",memberService.deleteMember(principal.getName())), HttpStatus.OK);
    }


    @GetMapping("/health_check")
    @Timed(value = "users.status", longTask = true)
    public String status() {
        return String.format("It's Working in User Service"
                + ", port(local.server.port) = " + env.getProperty("local.server.port")
                + ", port(server.port) = " + env.getProperty("server.port")
                + ", token secret  = " + env.getProperty("token.secret")
                + ", token expiration time= " + env.getProperty("token.expiration_time")
        );
    }

    @PostMapping("/info")
    public ResponseEntity<?> info(Principal principal) {
        log.info("회원정보조회");
        return new ResponseEntity<>(
                new CommonResDto<>(1,"회원조회성공",memberService.getMemberInfo(principal.getName())),HttpStatus.OK
        );
    }

}