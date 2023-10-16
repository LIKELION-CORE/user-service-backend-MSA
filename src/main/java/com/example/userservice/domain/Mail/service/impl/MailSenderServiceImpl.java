package com.example.userservice.domain.Mail.service.impl;

import com.example.userservice.domain.Mail.dto.PasswordChangeMailDto;
import com.example.userservice.domain.Mail.dto.VerificationMailDto;
import com.example.userservice.domain.Mail.service.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender javaMailSender;
    private SpringTemplateEngine templateEngine;

    @Autowired
    public MailSenderServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine){
        this.javaMailSender = javaMailSender;
        this.templateEngine = springTemplateEngine;
    }

    @Override
    public void sendNewPasswordMail(PasswordChangeMailDto passwordChangeMailDto) throws MessagingException {

    }

    @Override
    public void sendVerificationMail(VerificationMailDto verificationMailDto) throws MessagingException {
        String verificationNum = verificationMailDto.getCode();
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(verificationMailDto.getEmail());
        helper.setSubject("강남대학교 멋쟁이사자처럼 인증메일");

        //템플릿에 전달할 데이터 설정
        Context context = new Context();
        context.setVariable("name", verificationNum);

        //메일 내용 설정 : 템플릿 프로세스
        String html = templateEngine.process("acceptEmail.html",context);
        helper.setText(html, true);

        //helper.addInline("image1", new ClassPathResource("templates/images/_.png"));
        //helper.addInline("image2", new ClassPathResource("templates/images/.jpg"));

        //메일 보내기
        javaMailSender.send(message);
    }
}
