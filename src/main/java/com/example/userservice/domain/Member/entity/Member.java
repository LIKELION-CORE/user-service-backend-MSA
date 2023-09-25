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
        private String email;
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

        @Builder
        public Member(String email, String name, String userId, String password,String phone,boolean state,MemberRole memberRole) {
                this.email = email;
                this.name = name;
                this.userId = userId;
                this.password = password;
                this.phone=phone;
                this.state=state;
                this.memberRole=memberRole;
        }

        public void updateMember(UpdateMemberRequesstDto updateMemberRequesstDto) {
                this.name=updateMemberRequesstDto.getName();
                this.password=updateMemberRequesstDto.getPassword();
                this.memberRole=updateMemberRequesstDto.getMemberRole();
                this.phone=updateMemberRequesstDto.getPhone();

        }
}
