package com.example.userservice.domain.Mail;

public enum MailPurpose {
    FORGOT_PASSWORD, // 비밀번호 찾기
    SIGNUP, // 회원가입
    FIRST_PASS, //1차 합
    MISMATCH, // 불합
    MATCH // 최종 합
}
