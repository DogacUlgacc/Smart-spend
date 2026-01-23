package com.dogac.user_service.application.commands;

import java.util.List;

import com.dogac.user_service.application.core.Command;
import com.dogac.user_service.application.dto.CreatedUserResponse;
import com.dogac.user_service.domain.enums.UserType;
import com.dogac.user_service.domain.valueobjects.Address;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserCommand(
        @JsonProperty("firstName") @NotBlank(message = "First name is required") @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters") String firstName,

        @JsonProperty("lastName") @NotBlank(message = "Last name is required") @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters") String lastName,

        @JsonProperty("email") @NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email,

        @JsonProperty("phoneNumber") @NotBlank(message = "Phone number is required") String phoneNumber,

        @JsonProperty("userType") @NotNull(message = "User type is required") UserType userType,

        @JsonProperty("addresses") @Valid List<Address> addresses) implements Command<CreatedUserResponse> {
    public CreateUserCommand {
        addresses = (addresses == null) ? List.of() : List.copyOf(addresses);
    }
}