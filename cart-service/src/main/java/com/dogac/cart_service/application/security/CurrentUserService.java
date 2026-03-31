package com.dogac.cart_service.application.security;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.dogac.cart_service.domain.valueobjects.UserId;

@Service
public class CurrentUserService {

    public UserId getUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UserId.from(UUID.fromString(jwt.getSubject()));
    }
}