package com.dogac.user_service.application.mapper;

import org.springframework.stereotype.Component;

import com.dogac.user_service.application.dto.UpdatedUserResponse;
import com.dogac.user_service.domain.entities.User;

@Component
public class UpdateUserMapper {

    public UpdatedUserResponse toResponse(User domain) {
        return new UpdatedUserResponse(
                domain.getFullName().firstName(),
                domain.getFullName().lastName(),
                domain.getEmail().toString(),
                domain.getPhoneNumber().toString(),
                domain.getUserType(),
                domain.getAddresses(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }
}
