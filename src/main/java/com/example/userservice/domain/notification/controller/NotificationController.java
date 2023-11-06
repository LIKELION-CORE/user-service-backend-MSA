package com.example.userservice.domain.notification.controller;


import com.example.userservice.domain.Member.dto.request.MemberRenewAccessTokenRequestDto;
import com.example.userservice.domain.Member.dto.response.MemberRenewAccessTokenResponseDto;
import com.example.userservice.domain.auth.cookie.CookieUtil;
import com.example.userservice.domain.notification.dto.request.NotificationEmailSaveRequestDto;
import com.example.userservice.domain.notification.service.NotificationService;
import com.example.userservice.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("")
    public ResponseEntity notificationEmailSave(@RequestBody NotificationEmailSaveRequestDto notificationEmailSaveRequestDto) {

        log.info("사용자 이메일 이름 저장");
        return new ResponseEntity<>(new CommonResDto<>(1,"이메일 저장성공",notificationService.saveEmail(notificationEmailSaveRequestDto)), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity notificationUserList() {

        log.info("notificationUserList");
        return new ResponseEntity<>(new CommonResDto<>(1,"알림신청 유저리스트",notificationService.notificationUserList()), HttpStatus.OK);
    }
}
