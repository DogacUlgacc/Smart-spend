package com.dogac.user_service.application.mapper;

import org.springframework.stereotype.Component;

import com.dogac.user_service.application.dto.UserResponse;
import com.dogac.user_service.domain.entities.User;

@Component
public class UserResponseMapper {

    public UserResponse toResponse(User user) {

        return new UserResponse(
                user.getFullName().firstName(),
                user.getFullName().lastName(),
                user.getEmail().getValue(),
                user.getPhoneNumber().getValue(),
                user.getUserType(),
                user.getAddresses(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

}
