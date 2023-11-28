package com.example.userservice.domain.Member.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteMemberByAdminRequestDto {

    @NotEmpty
    @Size(min = 4, max = 32, message = "아이디를 4~32글자로 설정해주세요.")
    private String email;
}