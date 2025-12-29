package com.dogac.product_service.application.queryHandlers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dogac.product_service.application.core.QueryHandler;
import com.dogac.product_service.application.dto.ProductResponse;
import com.dogac.product_service.application.mapper.GetProductMapper;
import com.dogac.product_service.application.queries.GetProductListQuery;
import com.dogac.product_service.domain.entities.Product;
import com.dogac.product_service.domain.repositories.ProductRepository;

@Component
public class GetProductListQueryHandler
        implements QueryHandler<GetProductListQuery, List<ProductResponse>> {
    private final ProductRepository productRepository;
    private final GetProductMapper mapper;

    public GetProductListQueryHandler(ProductRepository productRepository, GetProductMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ProductResponse> handle(GetProductListQuery query) {
        List<Product> entityList = productRepository.findAll();
        return entityList.stream().map(mapper::toResponse).toList();
    }
}
