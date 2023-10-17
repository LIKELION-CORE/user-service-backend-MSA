package com.example.userservice.domain.Member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateMemberResponseDto {
    private String userId;
    private String name;
    private String phone;
    private Integer studentId;
    private String department;


    @Builder
    public CreateMemberResponseDto(String userId, String name,String phone,Integer studentId,String department) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.studentId=studentId;
        this.department=department;
    }
}
