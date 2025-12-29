package com.dogac.product_service.application.queries;

import java.util.List;

import com.dogac.product_service.application.core.Query;
import com.dogac.product_service.application.dto.ProductResponse;

public record GetProductListQuery() implements Query<List<ProductResponse>> {

}
