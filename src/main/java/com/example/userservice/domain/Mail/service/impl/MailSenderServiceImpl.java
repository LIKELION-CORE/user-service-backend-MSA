package com.example.userservice.domain.Mail.service.impl;

import com.example.userservice.domain.Mail.dto.request.SendMailDto;
import com.example.userservice.domain.Mail.dto.request.VerificationMailDto;
import com.example.userservice.domain.Mail.service.MailSenderService;
import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.Member.repository.MemberRepository;
import com.example.userservice.global.config.redis.util.EmailRedisUtil;
import com.example.userservice.global.exception.error.EmailNotValidException;
import com.example.userservice.global.exception.error.NotFoundAccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailRedisUtil emailRedisUtil;
    private final MemberRepository memberRepository;


//    @Override
//    public void sendNewPasswordMail(PasswordChangeMailDto passwordChangeMailDto) throws MessagingException {
//
//    }

    @Override
    @Async
    public void sendRandomNumberToMail(SendMailDto sendMailDto) throws MessagingException {
        String email=sendMailDto.getEmail();
        Integer randomNumber=getVerificationNumber();

        if (emailRedisUtil.existData(email)) emailRedisUtil.deleteData(email);


        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("강남대학교 멋쟁이사자처럼 인증메일");

        //템플릿에 전달할 데이터 설정
        Context context = new Context();
        context.setVariable("name", randomNumber);

        //메일 내용 설정 : 템플릿 프로세스
        String html = templateEngine.process("acceptEmail.html",context);
        helper.setText(html, true);

        //helper.addInline("image1", new ClassPathResource("templates/images/_.png"));
        //helper.addInline("image2", new ClassPathResource("templates/images/.jpg"));
        //메일 보내기
        javaMailSender.send(message);
        emailRedisUtil.setListData(email,randomNumber,60*3L);
    }

    @Override
    @Transactional
    public void sendVerificationMail(VerificationMailDto verificationMailDto) throws MessagingException {
        String userInputCode=verificationMailDto.getRandomNumber();
        String email = verificationMailDto.getEmail();

        List<String> info = emailRedisUtil.getData(verificationMailDto.getEmail());

        if(info.isEmpty() || info.get(0)==null){
            throw new EmailNotValidException("번호가 유효하지 않습니다.");
        }

        String[] splitInfo = info.get(0).split("\\|");
        String verificationCode = splitInfo[0];

        if(userInputCode.equals(verificationCode)){
            String newPassword = sendTemporaryPassword(email);
            updateMemberPassword(email, newPassword);
        }
    }

    private void updateMemberPassword(String email, String newPassword) {
        Member member = memberRepository.findMemberByUserId(email).orElseThrow(NotFoundAccountException::new);
        member.updatePassword(new BCryptPasswordEncoder().encode(newPassword));
    }

    private String sendTemporaryPassword(String email) throws MessagingException {
        String newPassword=getRandomPassword();
        SendMailDto sendMailDto= SendMailDto.builder()
                .email(email)
                .build();
        sendNewPasswordMail(sendMailDto,newPassword);
        return newPassword;
    }
    protected void sendNewPasswordMail(SendMailDto sendMailDto, String randomPassword) throws MessagingException {

        String email=sendMailDto.getEmail();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("강남대학교 멋쟁이사자처럼 인증메일");

        //템플릿에 전달할 데이터 설정
        Context context = new Context();
        context.setVariable("name", randomPassword);

        //메일 내용 설정 : 템플릿 프로세스
        String html = templateEngine.process("acceptEmail.html",context);
        helper.setText(html, true);
        //helper.addInline("image1", new ClassPathResource("templates/images/_.png"));
        //helper.addInline("image2", new ClassPathResource("templates/images/.jpg"));
        //메일 보내기
        javaMailSender.send(message);
    }

    private Integer getVerificationNumber() {
        // 난수의 범위 111111 ~ 999999 (6자리 난수)
        Random r = new Random();
        Integer checkNum = r.nextInt(888888) + 111111;

        return checkNum;
    }

    private String getRandomPassword() {
        int PASSWORD_LENGTH = 12;
        String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890123456789!@#$%^&*";

        SecureRandom random = new SecureRandom();

        String password = random.ints(PASSWORD_LENGTH, 0, PASSWORD_CHARACTERS.length())
                .mapToObj(PASSWORD_CHARACTERS::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

        return password;
    }
}
