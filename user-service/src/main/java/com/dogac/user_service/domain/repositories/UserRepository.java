package com.dogac.user_service.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.valueobjects.Email;
import com.dogac.user_service.domain.valueobjects.PhoneNumber;
import com.dogac.user_service.domain.valueobjects.UserId;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId id);

    List<User> findAll();

    void deleteById(UserId id);

    boolean existsById(UserId id);

    boolean existsByEmail(Email email);

    boolean existsByPhoneNumber(PhoneNumber phoneNumber);
}
