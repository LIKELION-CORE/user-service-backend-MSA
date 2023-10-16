package com.example.userservice.domain.Mail.controller;

import com.example.userservice.domain.Mail.dto.VerificationMailDto;
import com.example.userservice.domain.Mail.service.MailSenderService;
import com.example.userservice.domain.Member.dto.response.MemberInfoResponseDto;
import com.netflix.discovery.converters.Auto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("/api/v1/mail")
public class MailSenderController {

    private final MailSenderService mailSenderService;

    @Autowired
    public MailSenderController(MailSenderService mailSenderService){
        this.mailSenderService = mailSenderService;
    }

    //Test controller
    @GetMapping()
    public void readMemberInfo() throws MessagingException {

        mailSenderService.sendVerificationMail(
                VerificationMailDto.builder()
                        .email("scg9268@naver.com")
                        .name("성창규")
                        .code("123445678910")
                        .build());
    }
}
