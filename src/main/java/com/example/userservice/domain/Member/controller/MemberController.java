package com.example.userservice.domain.Member.controller;


import com.example.userservice.domain.Member.dto.request.MemberRenewAccessTokenRequestDto;
import com.example.userservice.domain.Member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.Member.dto.request.UpdateMemberRequesstDto;
import com.example.userservice.domain.Member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.Member.dto.response.MemberInfoResponseDto;
import com.example.userservice.domain.Member.dto.response.MemberRenewAccessTokenResponseDto;
import com.example.userservice.domain.Member.service.MemberService;
import com.example.userservice.domain.auth.cookie.CookieUtil;
import com.example.userservice.domain.auth.jwt.JwtProvider;
import com.example.userservice.global.common.CommonResDto;
import com.example.userservice.global.exception.error.NotFoundAccountException;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/member")
public class MemberController {
    private final Environment env;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;


    @PostMapping("/renew-access-token")
    public ResponseEntity renewAccessToken(@RequestBody MemberRenewAccessTokenRequestDto memberRenewAccessTokenRequestDto, HttpServletResponse response, Authentication authentication) {
        String accessToken = memberService.renewAccessToken(memberRenewAccessTokenRequestDto.getRefreshToken(),authentication);
        CookieUtil.addCookie(response, "accessToken", accessToken, jwtProvider.ACCESS_TOKEN_EXPIRATION_TIME);

        // token body comment
        return ResponseEntity.ok(MemberRenewAccessTokenResponseDto.builder()
                .accessToken(accessToken).build()
        );
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
    public ResponseEntity<?> updateMember(Principal principal,@Valid @RequestBody UpdateMemberRequesstDto updateMemberRequesstDto) {

        log.info("회원수정 진행 중");
        if(principal==null){
            throw new NotFoundAccountException("유저를 찾을 수 없습니다");
        }
        memberService.updateMember(principal.getName(),updateMemberRequesstDto);
        return new ResponseEntity<>(new CommonResDto<>(1,"회원수정완료",""), HttpStatus.OK);
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

        return new ResponseEntity<>(
                new CommonResDto<>(1,"회원조회성공",memberService.getMemberInfo(principal.getName())),HttpStatus.OK
        );
    }

}