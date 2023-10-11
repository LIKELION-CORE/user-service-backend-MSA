package com.example.userservice.domain.Mail.service.impl;

import com.example.userservice.domain.Mail.dto.PasswordChangeMailDto;
import com.example.userservice.domain.Mail.dto.VerificationMailDto;
import com.example.userservice.domain.Mail.service.MailSenderService;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class MailSenderServiceImpl implements MailSenderService {
    @Override
    public void sendNewPasswordMail(PasswordChangeMailDto passwordChangeMailDto) throws MessagingException {

    }

    @Override
    public void sendVerificationMail(VerificationMailDto verificationMailDto) throws MessagingException {

    }
}
