package com.dogac.user_service.infrastructure.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.repositories.UserRepository;
import com.dogac.user_service.domain.valueobjects.Email;
import com.dogac.user_service.domain.valueobjects.PhoneNumber;
import com.dogac.user_service.domain.valueobjects.UserId;
import com.dogac.user_service.infrastructure.mapper.UserEntityMapper;
import com.dogac.user_service.infrastructure.repositories.SpringDataUserRepository;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;
    private final UserEntityMapper userEntityMapper;

    public UserRepositoryAdapter(SpringDataUserRepository springDataUserRepository, UserEntityMapper userEntityMapper) {
        this.springDataUserRepository = springDataUserRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public User save(User user) {
        var entity = userEntityMapper.toEntity(user);
        entity = springDataUserRepository.save(entity);
        return userEntityMapper.toDomain(entity);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return springDataUserRepository.findById(id.value()).map(userEntityMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return springDataUserRepository.findAll().stream().map(userEntityMapper::toDomain).toList();
    }

    @Override
    public void deleteById(UserId id) {
        springDataUserRepository.deleteById(id.value());
    }

    @Override
    public boolean existsById(UserId id) {
        return springDataUserRepository.existsById(id.value());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return springDataUserRepository.existsByEmail(email.value());
    }

    @Override
    public boolean existsByPhoneNumber(PhoneNumber phoneNumber) {
        return springDataUserRepository.existsByPhoneNumber(phoneNumber.value());
    }
}
