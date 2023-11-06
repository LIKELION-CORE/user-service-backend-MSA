package com.example.userservice.domain.Member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.integration.annotation.InboundChannelAdapter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoResponseDto {
    private Long id;
    private String name;
    private Integer studentId;
    private String department;
    private String phone;
    private String email;

    @Builder
    public MemberInfoResponseDto(Long id, String name, Integer studentId, String department, String phone, String email) {
        this.id = id;
        this.name = name;
        this.studentId = studentId;
        this.department = department;
        this.phone = phone;
        this.email = email;
    }
}
