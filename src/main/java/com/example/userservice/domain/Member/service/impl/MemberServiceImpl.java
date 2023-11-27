package com.example.userservice.domain.Member.service.impl;

import com.example.userservice.domain.Member.dto.request.*;
import com.example.userservice.domain.Member.dto.response.CreateMemberResponseDto;
import com.example.userservice.domain.Member.dto.response.MemberInfoResponseDto;
import com.example.userservice.domain.Member.dto.response.MemberListGetAllByAdmin;
import com.example.userservice.domain.Member.entity.Member;
import com.example.userservice.domain.Member.repository.dao.MemberDao;
import com.example.userservice.domain.Member.service.MemberService;
import com.example.userservice.domain.auth.entity.RefreshToken;
import com.example.userservice.domain.auth.jwt.JwtProvider;
import com.example.userservice.domain.auth.repository.RefreshTokenRepository;
import com.example.userservice.global.common.CommonResDto;
import com.example.userservice.global.config.redis.util.EmailRedisUtil;
import com.example.userservice.global.exception.error.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {


    private final MemberDao memberDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailRedisUtil emailRedisUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;


    @Override
    @Transactional
    public String renewAccessToken(String refreshToken,Authentication authentication) {
        log.info("refreshToken = " + refreshToken);
        if (!jwtProvider.verifyToken(refreshToken)) {
            throw new InvalidTokenException();
        }
        String username = jwtProvider.getUsernameFromToken(refreshToken);
        RefreshToken refreshTokenFound = refreshTokenRepository.findById(username).orElseThrow(NotFoundAccountException::new);
        if (!refreshTokenFound.getToken().equals(refreshToken)) {
            throw new RuntimeException("not matching refreshToken");
        }

        return jwtProvider.generateAccessToken(username,authentication);
    }

    @Transactional
    public Long updateMemberByAdmin(String userId, UpdateMemberByAdminRequestDto updateMemberByAdminRequestDto) {
        String targetUserId=updateMemberByAdminRequestDto.getUserId();
        Member member = memberDao.findMemberByUserId(targetUserId);
        member.updateMemberByAdmin(updateMemberByAdminRequestDto);
        return member.getId();
    }

    @Override
    @Transactional
    public Long memberPasswordUpdate(String name, UpdateMemberPasswordRequestDto updateMemberPasswordRequestDto) {
        String currentPassword= updateMemberPasswordRequestDto.getCureentPassword();
        String changePassword= updateMemberPasswordRequestDto.getChangePassword();

        Member member = memberDao.findMemberByUserId(name);
        boolean passwordValidationCheck = passwordValidationCheck(currentPassword, member);
        if(passwordValidationCheck){
            member.updatePassword(new BCryptPasswordEncoder().encode(changePassword));
        }else{
            throw new PasswordNotMatchException("패스워드가 일치하지 않습니다");
        }

        return member.getId();
    }

    @Override
    public List<MemberListGetAllByAdmin> readAllMemberList() {
        List<Member> allMember = memberDao.findAllMember();

        List<MemberListGetAllByAdmin> result = allMember.stream().map(member -> new MemberListGetAllByAdmin(member))
                .collect(Collectors.toList());

        return result;
    }


    @Transactional
    public CommonResDto<CreateMemberResponseDto> createMember(SignUpRequestDto signUpRequestDto) {

        //중복체크검사
        signupVaidate(signUpRequestDto);
        // 이메일인증 여부
        emailVerifyCheck(signUpRequestDto);

        //회원가입 및 db save
        Member savedMember = registerMember(signUpRequestDto);


        return new CommonResDto<>(1,"회원가입성공",CreateMemberResponseDto.builder()
                .userId(savedMember.getUserId())
                .phone(savedMember.getPhone())
                .name(savedMember.getName())
                .department(savedMember.getDepartment())
                .studentId(savedMember.getStudentId())
                .build());
    }

    private void emailVerifyCheck(SignUpRequestDto signUpRequestDto) {
        log.info("회원가입 이메일 검증 중");

        List<String> data = emailRedisUtil.getData(signUpRequestDto.getEmail());
        if (data.isEmpty()){
            throw new EmailNotValidException();
        }
        String[] splitInfo = data.get(0).split("\\|");
        String redisVerifyPurpose = splitInfo[1];
        if(!Objects.equals(redisVerifyPurpose, "signupVerifySuccess")){
            throw new EmailNotValidException();
        }
    }

    public Member findMemberByUserId(String userId) {

        return memberDao.findMemberByUserId(userId);
    }

    public MemberInfoResponseDto getMemberInfo(String userId) {
        Member member = memberDao.findMemberByUserId(userId);
        return MemberInfoResponseDto.builder()
                .id(member.getId())
                .email(member.getUserId())
                .department(member.getDepartment())
                .phone(member.getPhone())
                .name(member.getName())
                .studentId(member.getStudentId())
                .build();
    }

    @Transactional
    public Long updateMember(String memberId,UpdateMemberRequesstDto updateMemberRequesstDto) {

        Member member = memberDao.findMemberByUserId(memberId);
        member.updateMember(updateMemberRequesstDto);
        return member.getId();
    }

    private boolean passwordValidationCheck(String userPassword, Member member) {
        if(bCryptPasswordEncoder.matches(userPassword, member.getPassword())){
            return true;
        }
        return false;
    }

    @Transactional
    public Long deleteMember(DeleteMemberRequestDto deleteMemberRequestDto, String memberId) {
        Member member = memberDao.findMemberByUserId(memberId);
        System.out.println(member.getPassword());
        if(new BCryptPasswordEncoder().matches(deleteMemberRequestDto.getPassword(),member.getPassword())){
            memberDao.deleteById(member.getId());
        }else{
            throw new PasswordNotMatchException();
        }

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

        String userId = signUpRequestDto.getEmail();
        if (memberDao.duplicateMemberCheck(userId).isPresent()) {
            throw new DuplicateAccountException("아이디 중복");
        }
    }


}
