package com.example.userservice.domain.Member.controller;


import com.example.userservice.domain.Member.dto.request.SignUpRequestDto;
import com.example.userservice.domain.Member.dto.response.MemberInfoResponseDto;
import com.example.userservice.domain.Member.service.MemberService;
import com.example.userservice.global.common.CommonResDto;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;


@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final Environment env;
    private final MemberService memberService;


    @GetMapping("/health_check")
    @Timed(value = "users.status", longTask = true)
    public String status() {
        return String.format("It's Working in User Service"
                + ", port(local.server.port) = " + env.getProperty("local.server.port")
                + ", port(server.port) = " + env.getProperty("server.port")
                + ", token secret  = " + env.getProperty("token.secret")
                + ", token expiration time= " + env.getProperty("token.expiration_time")
        );
    }

    @PostMapping("/info")
    public ResponseEntity<?> info(Principal principal) {

        return new ResponseEntity<>(
                new CommonResDto<>(1,"회원조회성공",memberService.getMemberInfo(principal.getName())),HttpStatus.OK
        );
    }

    @PostMapping("/memberInfo")
    public ResponseEntity<MemberInfoResponseDto> member(Principal principal) {
        return new ResponseEntity<>(
                memberService.getMemberInfo(principal.getName()),HttpStatus.OK
        );
        //return memberService.getMemberInfo(principal.getName());
    }

    @PostMapping("/join")
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {

        log.info("회원가입 진행 중");
        return new ResponseEntity<>(new CommonResDto<>(1,"회원생성완료",memberService.createUser(signUpRequestDto)), HttpStatus.CREATED);
    }


//    @GetMapping("/users")
//    public ResponseEntity<List<ResponseUser>> getUsers() {
//        Iterable<UserEntity> userList = userService.getUserByAll();
//        List<ResponseUser> result = new ArrayList<>();
//        userList.forEach(v -> result.add(new ModelMapper().map(v, ResponseUser.class)));
//
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
//
//    @GetMapping("/users/{userId}")
//    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
//        UserDto userDto = userService.getUserByUserId(userId);
//
//        ResponseUser responseUser = new ModelMapper().map(userDto, ResponseUser.class);
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
//    }
    //    @GetMapping("/welcome")
//    @Timed(value = "users.walcome", longTask = true)
//    public String welcome() {
//        log.info("test");
//        return greeting.getMessage();
//    }

}