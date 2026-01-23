package com.dogac.user_service.web;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dogac.user_service.application.bus.CommandBus;
import com.dogac.user_service.application.commands.CreateUserCommand;
import com.dogac.user_service.application.commands.UpdateUserCommand;
import com.dogac.user_service.application.dto.CreatedUserResponse;
import com.dogac.user_service.application.dto.UpdateUserRequest;
import com.dogac.user_service.application.dto.UpdatedUserResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final CommandBus commandBus;

    public UserController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @PostMapping
    public CreatedUserResponse createUser(@RequestBody CreateUserCommand command) {
        return commandBus.send(command);
    }

    @PutMapping("/{id}")
    public UpdatedUserResponse updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserRequest request) {

        UpdateUserCommand command = new UpdateUserCommand(
                id,
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.userType());

        return commandBus.send(command);
    }

}
