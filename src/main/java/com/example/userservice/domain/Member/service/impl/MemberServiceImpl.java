package com.example.userservice.domain.Member.service.impl;

import com.example.userservice.domain.Member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.Member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.Member.dto.response.MemberInfoResponseDto;
import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.Member.repository.dao.MemberDao;
import com.example.userservice.domain.Member.service.MemberService;
import com.example.userservice.global.exception.error.DuplicateAccountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {


    private final MemberDao memberDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    public CreateMemberResponseDto createUser(SignUpRequestDto signUpRequestDto) {

        //중복체크검사
        signupVaidate(signUpRequestDto);
        //회원가입
        Member savedMember = registerMember(signUpRequestDto);


        return CreateMemberResponseDto.builder()
                .userId(savedMember.getUserId())
                .phone(savedMember.getPhone())
                .email(savedMember.getEmail())
                .name(savedMember.getName())
                .build();
    }


    /**
     * 유저아이디로 해당 유저상태가져오는 함수
     */
    @Override
    public Member findMemberByUserId(String userId) {

        return memberDao.findMemberByUserId(userId);
    }

    @Override
    public MemberInfoResponseDto getMemberInfo(String userId) {
        Member member = memberDao.findMemberByUserId(userId);
        return MemberInfoResponseDto.builder()
                .id(member.getId())
                .build();
    }


    /**
     * 회원등록
     */

    private Member registerMember(SignUpRequestDto signUpRequestDto){


        // 리팩토링 필요
        signUpRequestDto.setPassword(bCryptPasswordEncoder.encode(signUpRequestDto.getPassword()));
        Member member = signUpRequestDto.toEntity();

        return memberDao.insertMember(member);
    }


    /**
     * 회원유효성검증
     */
    private void signupVaidate(SignUpRequestDto signUpRequestDto){

        String userId = signUpRequestDto.getUserId();


        if (memberDao.duplicateMemberCheck(userId).isPresent()) {
            throw new DuplicateAccountException("아이디 중복");
        }

//
//        String email = signUpRequestDto.getEmail();
//        boolean isDuplicateNickname = memberDao.findByEmail(email) != null;
//
//        if (isDuplicateNickname) {
//            throw new DuplicateAccountException("이메일 중복");
//        }
    }


}
