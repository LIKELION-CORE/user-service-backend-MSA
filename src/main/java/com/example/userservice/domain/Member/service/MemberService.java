package com.example.userservice.domain.Member.service;

import com.example.userservice.domain.Member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.Member.dto.request.UpdateMemberByAdminRequestDto;
import com.example.userservice.domain.Member.dto.request.UpdateMemberPasswordRequestDto;
import com.example.userservice.domain.Member.dto.request.UpdateMemberRequesstDto;
import com.example.userservice.domain.Member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.Member.dto.response.MemberInfoResponseDto;
import com.example.userservice.domain.Member.dto.response.MemberListGetAllByAdmin;
import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.global.common.CommonResDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MemberService {

    CommonResDto<CreateMemberResponseDto> createMember(SignUpRequestDto signUpRequestDto);
    Member findMemberByUserId(String username);
    MemberInfoResponseDto getMemberInfo(String name);

    Long updateMember(String memberId,UpdateMemberRequesstDto updateMemberRequesstDto);
    Long deleteMember(String userId);

    String renewAccessToken(String refreshToken, Authentication authentication);

    Long updateMemberByAdmin(String userId, UpdateMemberByAdminRequestDto updateMemberByAdminRequestDto);

    Long memberPasswordUpdate(String name, UpdateMemberPasswordRequestDto updateMemberPasswordRequestDto);

    List<MemberListGetAllByAdmin> readAllMemberList();
}
