package com.dogac.cart_service.application.core;

public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
}
