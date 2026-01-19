package com.dogac.product_service.domain.services;

import com.dogac.product_service.domain.entities.Product;
import com.dogac.product_service.domain.exceptions.DuplicateProductException;
import com.dogac.product_service.domain.exceptions.InsufficientStockException;
import com.dogac.product_service.domain.exceptions.ProductNotFoundException;
import com.dogac.product_service.domain.repositories.ProductRepository;
import com.dogac.product_service.domain.valueobjects.Description;
import com.dogac.product_service.domain.valueobjects.Money;
import com.dogac.product_service.domain.valueobjects.ProductId;
import com.dogac.product_service.domain.valueobjects.ProductName;
import com.dogac.product_service.domain.valueobjects.StockQuantity;

public class ProductDomainService {
    private final ProductRepository productRepository;

    public ProductDomainService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductName name, Description description, Money money,
            StockQuantity stockQuantity) {

        return Product.create(name, description, money, stockQuantity);
    }

    public void ensureProductNameIsUnique(String productName) {
        if (productRepository.existsByName(productName)) {
            throw new DuplicateProductException(
                    "Product with name '" + productName + "' already exists.");
        }
    }

    public void reserveStock(Product product, StockQuantity quantity) {
        if (!product.hasEnoughStock(quantity.value())) {
            throw new InsufficientStockException(
                    "Insufficient stock for product: " + product.getName().value());
        }
        product.decreaseStock(quantity);
    }

    public void releaseStock(Product product, StockQuantity quantity) {
        product.increaseStock(quantity);
    }

    public Product updateProduct(ProductId id, ProductName productName, Description productDescription, Money money,
            StockQuantity stockQuantity) {

        Product p = getProduct(id);
        p.updateName(productName);
        p.updateDescription(productDescription);
        p.updatePrice(money);
        p.updateStockQuantity(stockQuantity);
        return p;
    }

    public Product getProduct(ProductId id) {
        Product p = productRepository.findById(id).orElse(null);
        if (p == null) {
            throw new ProductNotFoundException("Product with given id: " + id + " not found!");
        }
        return p;
    }
}
