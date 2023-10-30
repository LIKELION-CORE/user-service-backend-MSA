package com.example.userservice.domain.Mail.service;

import com.example.userservice.domain.Mail.dto.request.SendMailDto;
import com.example.userservice.domain.Mail.dto.request.VerificationMailDto;

import javax.mail.MessagingException;

public interface MailSenderService {
    //인증 번호 전송 메소드
    void sendRandomNumberToMail(SendMailDto sendMailDto) throws MessagingException, javax.mail.MessagingException;

    void sendVerificationMail(VerificationMailDto verificationMailDto) throws MessagingException, javax.mail.MessagingException;

    void sendPassOrFailSendEmail(SendMailDto sendMailDto) throws MessagingException, javax.mail.MessagingException;
    //이건 일단 무시해주세요!
    //void sendFailMail(List<AcceptEmailDto> acceptEmailDto) throws MessagingException;

}
