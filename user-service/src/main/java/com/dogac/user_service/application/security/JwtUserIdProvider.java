package com.dogac.user_service.application.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.dogac.user_service.domain.valueobjects.UserId;

@Component
public class JwtUserIdProvider {
    public UserId currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("No authenticated user found");
        }

        String sub = jwt.getSubject();
        return UserId.fromKeycloakSub(sub);
    }
}
