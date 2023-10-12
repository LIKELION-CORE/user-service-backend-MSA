package com.example.userservice.domain.Mail.service;

import com.example.userservice.domain.Mail.dto.PasswordChangeMailDto;
import com.example.userservice.domain.Mail.dto.VerificationMailDto;

import javax.mail.MessagingException;
import java.util.List;

public interface MailSenderService {
    //임시 비밀번호 전송 메소드
    void sendNewPasswordMail(PasswordChangeMailDto passwordChangeMailDto) throws MessagingException;
    //인증 번호 전송 메소드
    void sendVerificationMail(VerificationMailDto verificationMailDto) throws MessagingException, javax.mail.MessagingException;

    //이건 일단 무시해주세요!
    //void sendFailMail(List<AcceptEmailDto> acceptEmailDto) throws MessagingException;

}
