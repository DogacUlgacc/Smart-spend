package com.dogac.user_service.infrastructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AddressEmbeddable {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "country", nullable = false)
    private String country;

    public AddressEmbeddable() {
        // Required by JPA
    }

    public AddressEmbeddable(String title, String city, String street, String country) {
        this.title = title;
        this.city = city;
        this.street = street;
        this.country = country;
    }

    public String getTitle() {
        return title;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getCountry() {
        return country;
    }
}