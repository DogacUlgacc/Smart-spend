package com.dogac.user_service.application.dto;

import com.dogac.user_service.domain.enums.UserType;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        UserType userType) {
}
