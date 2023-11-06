package com.example.userservice.domain.Mail.controller;

import com.example.userservice.domain.Mail.dto.request.SendMailDto;
import com.example.userservice.domain.Mail.dto.request.VerificationMailDto;
import com.example.userservice.domain.Mail.dto.response.PasswordChangeMailDto;
import com.example.userservice.domain.Mail.service.MailSenderService;
import com.example.userservice.global.common.CommonResDto;
import com.example.userservice.global.exception.error.EmailNotValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@Slf4j
@RequestMapping("/api/v1/mail")
public class MailSenderController {

    private final MailSenderService mailSenderService;

    @Autowired
    public MailSenderController(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }
    @PostMapping("/pass-fail")
    public void passOrFailSendEail(@RequestBody SendMailDto sendMailDto) throws MessagingException {
        log.info("합불 이메일 전송");
        mailSenderService.sendPassOrFailSendEmail(sendMailDto);
    }
    @PostMapping()
    public void sendEail(@RequestBody SendMailDto sendMailDto) throws MessagingException {
        log.info("이메일 전송");
        mailSenderService.sendMail(sendMailDto);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyRandomNumber(@RequestBody VerificationMailDto verificationMailDto) throws MessagingException {
        log.info("이메일 검증 및 임시 비밀번호 전송");
        try {
            mailSenderService.sendVerificationMail(verificationMailDto);
        }catch (Exception e){
            throw new EmailNotValidException();
        }
        return new ResponseEntity<>(new CommonResDto<>(1, "이메일 검증이 완료되었습니다.",""), HttpStatus.OK);

    }

}
