package com.dogac.cart_service.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dogac.cart_service.infrastructure.entities.JpaCartEntity;

public interface SpringDataCartRepository extends JpaRepository<JpaCartEntity, UUID> {
    Optional<JpaCartEntity> findByUserId(UUID userId);

    Optional<JpaCartEntity> findByIdAndUserId(UUID id, UUID userId);
}
