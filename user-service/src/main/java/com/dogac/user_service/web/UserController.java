package com.dogac.user_service.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dogac.user_service.application.bus.CommandBus;
import com.dogac.user_service.application.commands.CreateUserCommand;
import com.dogac.user_service.application.dto.CreatedUserResponse;

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
}
