package com.dogac.user_service.infrastructure.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dogac.user_service.infrastructure.entities.JpaUserEntity;

@Repository
public interface SpringDataUserRepository extends JpaRepository<JpaUserEntity, UUID> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
