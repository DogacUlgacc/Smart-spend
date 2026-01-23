package com.dogac.user_service.application.commands;

import java.util.UUID;

import com.dogac.user_service.application.core.Command;
import com.dogac.user_service.application.dto.UpdatedUserResponse;
import com.dogac.user_service.domain.enums.UserType;

public record UpdateUserCommand(
        UUID id,
        String firstName,
        String lastName,
        String phoneNumber,
        UserType userType) implements Command<UpdatedUserResponse> {

}
