package com.example.userservice.domain.Member.entity;

import com.example.userservice.domain.Member.dto.request.UpdateMemberRequesstDto;
import com.example.userservice.domain.auth.jwt.MemberRole;
import com.example.userservice.global.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER")
public class Member extends BaseTimeEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false, length = 50)
        private String name;
        @Column(nullable = false, unique = true)
        private String userId;
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Column(nullable = false)
        private String password;
        @Column(nullable = false)
        private String phone;
        private Boolean state;
        @Enumerated(EnumType.STRING)
        private MemberRole memberRole;
        private Integer studentId;
        private String department;

        @Builder
        public Member(String name, String userId, String password,String phone,boolean state,MemberRole memberRole,Integer studentId,String department) {
                this.name = name;
                this.userId = userId;
                this.password = password;
                this.phone=phone;
                this.state=state;
                this.memberRole=memberRole;
                this.studentId=studentId;
                this.department=department;
        }

        public void updatePassword(String password) {
                this.password = password;
        }

        public void updateMember(UpdateMemberRequesstDto updateMemberRequesstDto) {
                this.department=updateMemberRequesstDto.getDepartment();
                this.memberRole=updateMemberRequesstDto.getMemberRole();
                this.phone=updateMemberRequesstDto.getPhone();
                this.userId=updateMemberRequesstDto.getEmail();
                this.studentId=updateMemberRequesstDto.getStudentId();

        }
}
