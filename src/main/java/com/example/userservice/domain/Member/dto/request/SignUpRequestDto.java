package com.example.userservice.domain.Member.dto.request;

import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.auth.jwt.MemberRole;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {


    @NotEmpty(message = "이메일 인증은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotEmpty(message = "비밀번호 설정은 필수입니다.")
    @Size(min = 8, max = 64, message = "비밀번호를 8~64글자의 영문+숫자 조합으로 설정해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$", message = "비밀번호는 영문과 숫자의 조합이어야 합니다.")
    private String password;

    @NotEmpty(message = "이름 설정은 필수입니다.")
    @Size(min = 1, max = 12, message = "닉네임을 12글자 이하로 설정헤주세요.")
    private String name;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호를 입력해주세요.")
    private String phone;

    @NotNull(message = "학번은 필수입니다.")
    private Integer studentId;

    @NotEmpty(message = "학과는 필수입니다.")
    private String department;

    public Member toEntity() {
       return Member.builder()
               .password(password)
               .userId(email)
               .name(name)
               .phone(phone)
               .state(true)
               .memberRole(MemberRole.APPLY)
               .studentId(studentId)
               .department(department)
               .build();
    }

}
