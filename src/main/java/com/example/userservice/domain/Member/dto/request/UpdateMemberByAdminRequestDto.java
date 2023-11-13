package com.example.userservice.domain.Member.dto.request;

import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.auth.jwt.MemberRole;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateMemberByAdminRequestDto{

    @NotNull(message = "학번은 필수입니다.")
    private Integer studentId;

    @NotEmpty(message = "학과는 필수입니다.")
    private String department;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호를 입력해주세요.")
    private String phone;

    private MemberRole memberRole;

    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식이 맞지 않습니다.")
    private String userId;
}
