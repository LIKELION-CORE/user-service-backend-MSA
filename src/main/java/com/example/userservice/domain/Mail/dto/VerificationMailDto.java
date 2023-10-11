package com.example.userservice.domain.Mail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationMailDto {
    //보내려는 사람의 email
    String email;
    //이름
    String name;
    //인증번호
    String code;
}
