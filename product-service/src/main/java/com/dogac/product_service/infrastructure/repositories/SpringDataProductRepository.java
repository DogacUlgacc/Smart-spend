package com.dogac.product_service.infrastructure.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dogac.product_service.infrastructure.entities.JpaProductEntity;

@Repository
public interface SpringDataProductRepository extends JpaRepository<JpaProductEntity, UUID> {

    void deleteById(UUID id);

    boolean existsById(UUID id);

    boolean existsByProductName(String productName);

}
