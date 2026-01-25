package com.dogac.user_service.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

public record PhoneNumber(String value) {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+[1-9]\\d{7,14}$");

    public PhoneNumber {
        Objects.requireNonNull(value, "Phone number cannot be null");

        value = value.trim();

        if (value.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }

        if (!PHONE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid phone number format. Use E.164 format (e.g. +905551112233)");
        }
    }

    public String getValue() {
        return value;
    }
}
