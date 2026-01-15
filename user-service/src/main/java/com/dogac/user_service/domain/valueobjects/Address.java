package com.dogac.user_service.domain.valueobjects;

import java.util.Objects;

public record Address(
        String title,
        String city,
        String street,
        String country) {

    public Address {
        Objects.requireNonNull(title, "Address title cannot be null");
        Objects.requireNonNull(city, "City cannot be null");
        Objects.requireNonNull(street, "Street cannot be null");
        Objects.requireNonNull(country, "Country cannot be null");

        title = title.trim();
        city = city.trim();
        street = street.trim();
        country = country.trim();

        if (title.isEmpty())
            throw new IllegalArgumentException("Title empty");
        if (city.isEmpty())
            throw new IllegalArgumentException("City empty");
        if (street.isEmpty())
            throw new IllegalArgumentException("Street empty");
        if (country.isEmpty())
            throw new IllegalArgumentException("Country empty");
    }
}
