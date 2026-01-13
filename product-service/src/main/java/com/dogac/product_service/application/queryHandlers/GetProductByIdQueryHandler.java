package com.dogac.product_service.application.queryHandlers;

import org.springframework.stereotype.Component;

import com.dogac.product_service.application.core.QueryHandler;
import com.dogac.product_service.application.dto.ProductResponse;
import com.dogac.product_service.application.mapper.GetProductMapper;
import com.dogac.product_service.application.queries.GetProductByIdQuery;
import com.dogac.product_service.domain.entities.Product;
import com.dogac.product_service.domain.exceptions.ProductNotFoundException;
import com.dogac.product_service.domain.repositories.ProductRepository;
import com.dogac.product_service.domain.valueobjects.ProductId;

@Component
public class GetProductByIdQueryHandler implements QueryHandler<GetProductByIdQuery, ProductResponse> {

    private final ProductRepository productRepository;
    private final GetProductMapper getProductMapper;

    public GetProductByIdQueryHandler(ProductRepository productRepository, GetProductMapper getProductMapper) {
        this.productRepository = productRepository;
        this.getProductMapper = getProductMapper;
    }

    @Override
    public ProductResponse handle(GetProductByIdQuery query) {
        ProductId productId = ProductId.from(query.id());
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ProductNotFoundException("Product with given id: " + productId + " is not found!"));
        return getProductMapper.toResponse(product);
    }
}
