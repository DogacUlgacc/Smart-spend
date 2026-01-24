package com.dogac.user_service.application.commands;

import java.util.UUID;

import com.dogac.user_service.application.core.Command;

import jakarta.validation.constraints.NotBlank;

public record DeleteUserCommand(@NotBlank UUID id) implements Command<Void> {

}
