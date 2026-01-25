package com.dogac.user_service.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.dogac.user_service.application.commands.DeleteUserCommand;
import com.dogac.user_service.application.core.CommandHandler;
import com.dogac.user_service.domain.exceptions.UserNotFoundExcepiton;
import com.dogac.user_service.domain.repositories.UserRepository;
import com.dogac.user_service.domain.services.UserDomainService;
import com.dogac.user_service.domain.valueobjects.UserId;

@Component
public class DeleteUserCommandHandler implements CommandHandler<DeleteUserCommand, Void> {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;

    public DeleteUserCommandHandler(UserRepository userRepository, UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
    }

    @Override
    public Void handle(DeleteUserCommand command) {
        userRepository.findById(UserId.from(command.id()))
                .orElseThrow(() -> new UserNotFoundExcepiton("User with given id is not found!"));

        userRepository.deleteById(UserId.from(command.id()));
        return null;

    }

}
