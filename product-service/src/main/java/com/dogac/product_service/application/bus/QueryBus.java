package com.dogac.product_service.application.bus;

import com.dogac.product_service.application.core.Query;

public interface QueryBus {
    <R, Q extends Query<R>> R execute(Q query);
}