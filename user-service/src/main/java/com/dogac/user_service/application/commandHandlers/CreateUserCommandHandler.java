package com.dogac.user_service.application.commandHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dogac.user_service.application.commands.CreateUserCommand;
import com.dogac.user_service.application.core.CommandHandler;
import com.dogac.user_service.application.dto.CreatedUserResponse;
import com.dogac.user_service.application.mapper.CreateUserMapper;
import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.repositories.UserRepository;
import com.dogac.user_service.domain.services.UserDomainService;
import com.dogac.user_service.domain.valueobjects.Email;
import com.dogac.user_service.domain.valueobjects.PhoneNumber;

@Component
@Transactional
public class CreateUserCommandHandler
        implements CommandHandler<CreateUserCommand, CreatedUserResponse> {

    private final CreateUserMapper createUserMapper;
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;

    public CreateUserCommandHandler(CreateUserMapper createUserMapper, UserRepository userRepository,
            UserDomainService userDomainService) {
        this.createUserMapper = createUserMapper;
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
    }

    @Override
    public CreatedUserResponse handle(CreateUserCommand command) {

        User user = createUserMapper.toEntity(command);
        userDomainService.ensureEmailIsUnique(new Email(command.email()));
        userDomainService.ensurePhoneNumberIsUnique(new PhoneNumber(command.phoneNumber()));
        userRepository.save(user);

        return createUserMapper.toResponse(user);
    }
}
