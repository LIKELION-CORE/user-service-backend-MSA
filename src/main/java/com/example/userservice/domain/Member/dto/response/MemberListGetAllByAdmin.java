package com.example.userservice.domain.Member.dto.response;


import com.example.userservice.domain.Member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberListGetAllByAdmin {
    private Long id;
    private String name;
    private Integer studentId;
    private String department;
    private String phone;
    private String email;
    private String role;

    @Builder
    public MemberListGetAllByAdmin(Member member) {
        this.id=member.getId();
        this.name=member.getName();
        this.studentId=member.getStudentId();
        this.department=member.getDepartment();
        this.phone=member.getPhone();
        this.email=member.getUserId();
        this.role=member.getMemberRole().toString();
    }
}
