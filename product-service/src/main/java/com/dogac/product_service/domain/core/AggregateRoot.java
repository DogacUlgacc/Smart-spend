package com.dogac.product_service.domain.core;

public interface AggregateRoot<ID> {
    ID getId();
}