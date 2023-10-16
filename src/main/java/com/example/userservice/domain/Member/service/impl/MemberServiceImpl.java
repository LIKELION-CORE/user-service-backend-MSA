package com.example.userservice.domain.Member.service.impl;

import com.example.userservice.domain.Member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.Member.dto.request.UpdateMemberRequesstDto;
import com.example.userservice.domain.Member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.Member.dto.response.MemberInfoResponseDto;
import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.Member.repository.dao.MemberDao;
import com.example.userservice.domain.Member.service.MemberService;
import com.example.userservice.global.exception.error.DuplicateAccountException;
import com.example.userservice.global.exception.error.PasswordNotMatchException;
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
    public CreateMemberResponseDto createMember(SignUpRequestDto signUpRequestDto) {

        //중복체크검사
        signupVaidate(signUpRequestDto);
        //회원가입 및 db save
        Member savedMember = registerMember(signUpRequestDto);
        return CreateMemberResponseDto.builder()
                .userId(savedMember.getUserId())
                .phone(savedMember.getPhone())
                .name(savedMember.getName())
                .department(savedMember.getDepartment())
                .studentId(savedMember.getStudentId())
                .build();
    }

    public Member findMemberByUserId(String userId) {

        return memberDao.findMemberByUserId(userId);
    }

    public MemberInfoResponseDto getMemberInfo(String userId) {
        Member member = memberDao.findMemberByUserId(userId);
        return MemberInfoResponseDto.builder()
                .id(member.getId())
                .build();
    }

    @Transactional
    public Long updateMember(String memberId,UpdateMemberRequesstDto updateMemberRequesstDto) {
        String userPassword= updateMemberRequesstDto.getPassword();

        Member member = memberDao.findMemberByUserId(memberId);
        boolean passwordValid = passwordValidationCheck(userPassword, member);


        if(passwordValid){
            updateMemberRequesstDto.setPassword(bCryptPasswordEncoder.encode(userPassword));
            member.updateMember(updateMemberRequesstDto);
        }
        if(!passwordValid){
            throw new PasswordNotMatchException("비밀번호가 일치하지않습니다");
        }
        return member.getId();
    }

    private boolean passwordValidationCheck(String userPassword, Member member) {
        if(bCryptPasswordEncoder.matches(userPassword, member.getPassword())){
            return true;
        }
        return false;
    }

    @Transactional
    public Long deleteMember(String memberId) {
        Member member = memberDao.findMemberByUserId(memberId);
        memberDao.deleteById(member.getId());
        return member.getId();
    }


    /**
     * 회원등록
     */
    private Member registerMember(SignUpRequestDto signUpRequestDto){
        String password=signUpRequestDto.getPassword();
        signUpRequestDto.setPassword(bCryptPasswordEncoder.encode(password));
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
    }


}
