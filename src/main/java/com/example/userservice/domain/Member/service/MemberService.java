package com.example.userservice.domain.Member.service;

import com.example.userservice.domain.Member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.Member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.Member.dto.response.MemberInfoResponseDto;
import com.example.userservice.domain.Member.entity.Member;

import java.util.Optional;

public interface MemberService {

    CreateMemberResponseDto createUser(SignUpRequestDto signUpRequestDto);
    Member findMemberByUserId(String username);

    MemberInfoResponseDto getMemberInfo(String name);
}
