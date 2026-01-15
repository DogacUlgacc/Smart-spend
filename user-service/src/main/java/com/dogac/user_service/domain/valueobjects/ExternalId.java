package com.dogac.user_service.domain.valueobjects;

public record ExternalId(String value) {
    public ExternalId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("External ID boş olamaz.");
        }
    }
}