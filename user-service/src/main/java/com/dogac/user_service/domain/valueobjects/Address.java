package com.dogac.user_service.domain.valueobjects;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Address(
        @JsonProperty("title") String title,
        @JsonProperty("city") String city,
        @JsonProperty("street") String street,
        @JsonProperty("country") String country) {

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
