package com.example.userservice.domain.auth.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    MEMBER("ROLE_MEMBER"),GUEST("ROLE_GUEST"),ADMIN("ROLE_ADMIN");
    private final String key;
}
