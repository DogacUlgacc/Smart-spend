package com.dogac.user_service.domain.services;

import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.exceptions.DuplicatePhoneNumberException;
import com.dogac.user_service.domain.exceptions.DuplicateUserEmailException;
import com.dogac.user_service.domain.repositories.UserRepository;
import com.dogac.user_service.domain.valueobjects.Email;
import com.dogac.user_service.domain.valueobjects.FullName;
import com.dogac.user_service.domain.valueobjects.PhoneNumber;

public class UserDomainService {

    private final UserRepository userRepository;

    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void changeFullName(User user, FullName newFullName) {
        user.changeFullName(newFullName);
    }

    public void changePhoneNumber(User user, PhoneNumber newPhoneNumber) {
        ensurePhoneNumberIsUnique(newPhoneNumber);
        user.changePhoneNumber(newPhoneNumber);
    }

    public void ensureEmailIsUnique(Email email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateUserEmailException(
                    "Email  '" + email + "' already exists.");
        }
    }

    public void ensurePhoneNumberIsUnique(PhoneNumber phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicatePhoneNumberException(
                    "Phone number '" + phoneNumber + "'already existst.");
        }
    }

    public void activateUser(User user) {
        user.activate();
    }

    public void deactivateUser(User user) {
        user.block();
    }

}
