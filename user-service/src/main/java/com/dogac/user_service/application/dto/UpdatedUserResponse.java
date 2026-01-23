package com.dogac.user_service.application.dto;

import java.time.Instant;
import java.util.List;

import com.dogac.user_service.domain.enums.UserType;
import com.dogac.user_service.domain.valueobjects.Address;
import com.fasterxml.jackson.annotation.JsonFormat;

public record UpdatedUserResponse(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        UserType userType,
        List<Address> address,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC") Instant createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC") Instant updatedAt) {

}
