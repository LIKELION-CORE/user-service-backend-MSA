package com.example.userservice.domain.auth.jwt;

import com.example.userservice.domain.Member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
public class MemberDetails implements UserDetails {
    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("MemberDetails.getAuthorities");
        Collection<GrantedAuthority> collection=new ArrayList<>();
        collection.add(()->{
            return String.valueOf(member.getMemberRole());
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }


    @Override
    public String getUsername() {
        return member.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return member.getState();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO
        return true;
    }
}
