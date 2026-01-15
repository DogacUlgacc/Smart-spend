package com.dogac.user_service.domain.valueobjects;

import java.util.Objects;

public record FullName(String firstName, String lastName) {

    public FullName {
        Objects.requireNonNull(firstName, "First name cannot be null");
        Objects.requireNonNull(lastName, "Last name cannot be null");

        firstName = firstName.trim();
        lastName = lastName.trim();

        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }

        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }

        if (firstName.length() > 50) {
            throw new IllegalArgumentException("First name is too long");
        }

        if (lastName.length() > 50) {
            throw new IllegalArgumentException("Last name is too long");
        }
        if (!firstName.matches("^[\\p{L} .'-]+$")) {
            throw new IllegalArgumentException("Invalid characters in first name");
        }
    }

}
