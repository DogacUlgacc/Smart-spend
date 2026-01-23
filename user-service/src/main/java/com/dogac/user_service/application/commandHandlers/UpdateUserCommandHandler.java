package com.dogac.user_service.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.dogac.user_service.application.commands.UpdateUserCommand;
import com.dogac.user_service.application.core.CommandHandler;
import com.dogac.user_service.application.dto.UpdatedUserResponse;
import com.dogac.user_service.application.mapper.UpdateUserMapper;
import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.repositories.UserRepository;
import com.dogac.user_service.domain.services.UserDomainService;
import com.dogac.user_service.domain.valueobjects.FullName;
import com.dogac.user_service.domain.valueobjects.PhoneNumber;
import com.dogac.user_service.domain.valueobjects.UserId;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@Component
@Transactional
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand, UpdatedUserResponse> {

    private final UserRepository userRepository;
    private final UpdateUserMapper updateUserMapper;
    private final UserDomainService userDomainService;

    public UpdateUserCommandHandler(UserRepository userRepository, UpdateUserMapper updateUserMapper,
            UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.updateUserMapper = updateUserMapper;
        this.userDomainService = userDomainService;
    }

    @Override
    public UpdatedUserResponse handle(UpdateUserCommand command) {
        User user = userRepository.findById(UserId.from(command.id()))
                .orElseThrow(() -> new NotFoundException("User with given id is not found"));

        if (command.phoneNumber() != null) {
            userDomainService.ensurePhoneNumberIsUnique(
                    new PhoneNumber(command.phoneNumber()));
            user.changePhoneNumber(new PhoneNumber(command.phoneNumber()));
        }

        if (command.firstName() != null || command.lastName() != null) {
            user.changeFullName(
                    new FullName(command.firstName(), command.lastName()));
        }

        if (command.userType() != null) {
            user.changeUserType(command.userType());
        }

        userRepository.save(user);

        return updateUserMapper.toResponse(user);
    }

}
