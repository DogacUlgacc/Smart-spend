package com.dogac.product_service.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.dogac.product_service.domain.entities.Product;
import com.dogac.product_service.domain.valueobjects.ProductId;

public interface ProductRepository {
    Product save(Product product);
    
    Optional<Product> findById(ProductId id);
    
    List<Product> findAll();
    
    void deleteById(ProductId id);
    
    boolean existsById(ProductId id);
    
    boolean existsByName(String name);
}

