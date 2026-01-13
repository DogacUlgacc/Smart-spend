package com.dogac.product_service.application.commandHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dogac.product_service.application.commands.DeleteProductCommand;
import com.dogac.product_service.application.core.CommandHandler;
import com.dogac.product_service.domain.exceptions.ProductNotFoundException;
import com.dogac.product_service.domain.repositories.ProductRepository;
import com.dogac.product_service.domain.valueobjects.ProductId;

@Component
@Transactional
public class DeleteProductCommandHandler implements CommandHandler<DeleteProductCommand, Void> {

    private final ProductRepository productRepository;

    public DeleteProductCommandHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Void handle(DeleteProductCommand command) {
        ProductId id = ProductId.from(command.id());
        productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with given id: " + id + " is not found!"));
        productRepository.deleteById(id);
        return null;
    }

}
