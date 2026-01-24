package com.dogac.user_service.application.queries;

import java.util.UUID;

import com.dogac.user_service.application.core.Query;
import com.dogac.user_service.application.dto.UserResponse;

import jakarta.validation.constraints.NotNull;

public record GetUserByIdQuery(@NotNull UUID id) implements Query<UserResponse> {

}
