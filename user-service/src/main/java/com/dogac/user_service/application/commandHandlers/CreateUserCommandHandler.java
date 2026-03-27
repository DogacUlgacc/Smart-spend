package com.dogac.user_service.application.commandHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dogac.user_service.application.commands.CreateUserCommand;
import com.dogac.user_service.application.core.CommandHandler;
import com.dogac.user_service.application.dto.CreatedUserResponse;
import com.dogac.user_service.application.mapper.CreateUserMapper;
import com.dogac.user_service.application.security.JwtUserIdProvider;
import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.repositories.UserRepository;
import com.dogac.user_service.domain.services.UserDomainService;
import com.dogac.user_service.domain.valueobjects.Email;
import com.dogac.user_service.domain.valueobjects.PhoneNumber;
import com.dogac.user_service.domain.valueobjects.UserId;

@Component
@Transactional
public class CreateUserCommandHandler
        implements CommandHandler<CreateUserCommand, CreatedUserResponse> {

    private final CreateUserMapper createUserMapper;
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final JwtUserIdProvider jwtUserIdProvider;

    public CreateUserCommandHandler(CreateUserMapper createUserMapper, UserRepository userRepository,
            UserDomainService userDomainService, JwtUserIdProvider jwtUserIdProvider) {
        this.createUserMapper = createUserMapper;
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
        this.jwtUserIdProvider = jwtUserIdProvider;
    }

    @Override
    public CreatedUserResponse handle(CreateUserCommand command) {
        UserId userId = jwtUserIdProvider.currentUserId();
        User user = createUserMapper.toEntity(command, userId);
        userDomainService.ensureEmailIsUnique(new Email(command.email()));
        userDomainService.ensurePhoneNumberIsUnique(new PhoneNumber(command.phoneNumber()));
        userRepository.save(user);

        return createUserMapper.toResponse(user);
    }
}
