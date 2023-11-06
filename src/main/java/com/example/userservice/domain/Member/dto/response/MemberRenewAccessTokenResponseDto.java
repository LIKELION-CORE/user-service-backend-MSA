package com.example.userservice.domain.Member.dto.response;


import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberRenewAccessTokenResponseDto {
    String accessToken;
    String refreshToken;
}
