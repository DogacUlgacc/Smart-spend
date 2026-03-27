package com.dogac.user_service.application.mapper;

import org.springframework.stereotype.Component;

import com.dogac.user_service.application.commands.CreateUserCommand;
import com.dogac.user_service.application.dto.CreatedUserResponse;
import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.valueobjects.Email;
import com.dogac.user_service.domain.valueobjects.FullName;
import com.dogac.user_service.domain.valueobjects.PhoneNumber;
import com.dogac.user_service.domain.valueobjects.UserId;

@Component
public class CreateUserMapper {

    public User toEntity(CreateUserCommand command, UserId userId) {
        User user = User.create(
                userId,
                new FullName(command.firstName(), command.lastName()),
                new Email(command.email()),
                new PhoneNumber(command.phoneNumber()),
                command.userType());

        // Komuttaki adresleri domain objesine ekle
        command.addresses().forEach(user::addAddress);

        return user;
    }

    public CreatedUserResponse toResponse(User user) {
        return new CreatedUserResponse(user.getFullName().firstName(),
                user.getFullName().lastName(),
                user.getEmail().toString(),
                user.getPhoneNumber().toString(),
                user.getUserType(),
                user.getAddresses(),
                user.getCreatedAt(),
                user.getCreatedAt());
    }
}