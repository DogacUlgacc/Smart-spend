package com.dogac.user_service.domain.core;

public interface AggregateRoot<ID> {
    ID getId();
}