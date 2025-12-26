package com.dogac.product_service.application.queries;

import java.util.UUID;

import com.dogac.product_service.application.core.Query;
import com.dogac.product_service.application.dto.ProductResponse;

import jakarta.validation.constraints.NotNull;

public record GetProductByIdQuery(@NotNull UUID id) implements Query<ProductResponse> {

}
