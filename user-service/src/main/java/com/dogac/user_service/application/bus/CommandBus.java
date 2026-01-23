package com.dogac.user_service.application.bus;

import com.dogac.user_service.application.core.Command;

public interface CommandBus {
    <R, C extends Command<R>> R send(C command);
}