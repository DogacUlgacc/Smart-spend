package com.dogac.product_service.application.bus;

import com.dogac.product_service.application.core.Command;

public interface CommandBus {
    <R, C extends Command<R>> R send(C command);
}