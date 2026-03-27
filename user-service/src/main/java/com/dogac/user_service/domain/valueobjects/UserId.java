package com.dogac.user_service.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {
    public UserId {
        Objects.requireNonNull(value, "Value for UserId cannot be a null!");
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId from(UUID value) {
        return new UserId(value);
    }

    public static UserId fromKeycloakSub(String sub) {
        UUID uuid = UUID.fromString(sub);
        return new UserId(uuid);
    }

}
