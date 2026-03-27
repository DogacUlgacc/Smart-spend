package com.dogac.user_service.domain.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.dogac.user_service.domain.core.AggregateRoot;
import com.dogac.user_service.domain.enums.UserStatus;
import com.dogac.user_service.domain.enums.UserType;
import com.dogac.user_service.domain.valueobjects.Address;
import com.dogac.user_service.domain.valueobjects.Email;
import com.dogac.user_service.domain.valueobjects.ExternalId;
import com.dogac.user_service.domain.valueobjects.FullName;
import com.dogac.user_service.domain.valueobjects.PhoneNumber;
import com.dogac.user_service.domain.valueobjects.UserId;

public class User implements AggregateRoot<UserId> {

    private final UserId id;
    private ExternalId externalId;

    private FullName fullName;
    private Email email;
    private PhoneNumber phoneNumber;

    private UserType userType;
    private UserStatus status;

    private List<Address> addresses;

    private Instant createdAt;
    private Instant updatedAt;

    private User(
            UserId id,
            FullName fullName,
            Email email,
            PhoneNumber phoneNumber,
            UserType userType,
            UserStatus status,
            List<Address> addresses,
            Instant createdAt,
            Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.fullName = Objects.requireNonNull(fullName);
        this.email = Objects.requireNonNull(email);
        this.phoneNumber = phoneNumber;
        this.userType = Objects.requireNonNull(userType);
        this.status = Objects.requireNonNull(status);
        this.addresses = addresses;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // public static User create(
    // FullName fullName,
    // Email email,
    // PhoneNumber phoneNumber,
    // UserType userType) {
    // Instant now = Instant.now();

    // return new User(
    // UserId.generate(),
    // Objects.requireNonNull(fullName),
    // Objects.requireNonNull(email),
    // Objects.requireNonNull(phoneNumber),
    // Objects.requireNonNull(userType),
    // UserStatus.ACTIVE,
    // new ArrayList<>(),
    // now,
    // now);
    // }

    public static User create(
            UserId id,
            FullName fullName,
            Email email,
            PhoneNumber phoneNumber,
            UserType userType) {
        Instant now = Instant.now();

        return new User(
                id,
                Objects.requireNonNull(fullName),
                Objects.requireNonNull(email),
                Objects.requireNonNull(phoneNumber),
                Objects.requireNonNull(userType),
                UserStatus.ACTIVE,
                new ArrayList<>(),
                now,
                now);
    }

    public static User rehydrate(
            UserId id,
            ExternalId externalId,
            FullName fullName,
            Email email,
            PhoneNumber phoneNumber,
            UserType userType,
            UserStatus status,
            List<Address> addresses,
            Instant createdAt,
            Instant updatedAt) {
        User user = new User(
                id,
                fullName,
                email,
                phoneNumber,
                userType,
                status,
                addresses != null ? new ArrayList<>(addresses) : new ArrayList<>(),
                createdAt,
                updatedAt);
        user.externalId = externalId;
        return user;
    }

    public void changeFullName(FullName newFullName) {
        this.fullName = Objects.requireNonNull(newFullName);
        touch();
    }

    public void changePhoneNumber(PhoneNumber newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
        touch();
    }

    public void addAddress(Address address) {
        Objects.requireNonNull(address);

        if (addresses.contains(address)) {
            throw new IllegalStateException("Address already exists");
        }

        addresses.add(address);
        touch();
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        touch();
    }

    public void block() {
        if (this.status == UserStatus.BLOCKED) {
            throw new IllegalStateException("User already blocked");
        }
        this.status = UserStatus.BLOCKED;
        touch();
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
        touch();
    }

    public void blockUser() {
        this.status = UserStatus.BLOCKED;
        touch();
    }

    public void attachExternalIdentity(ExternalId externalId) {
        if (this.externalId != null) {
            throw new IllegalStateException("External identity already attached");
        }
        this.externalId = Objects.requireNonNull(externalId);
        touch();
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    @Override
    public UserId getId() {
        return id;
    }

    public ExternalId getExternalId() {
        return externalId;
    }

    public FullName getFullName() {
        return fullName;
    }

    public Email getEmail() {
        return email;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public UserType getUserType() {
        return userType;
    }

    public UserStatus getStatus() {
        return status;
    }

    public List<Address> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User other))
            return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void changeUserType(UserType userType) {
        if (userType == null) {
            throw new IllegalArgumentException("UserType cannot be null");
        }

        if (this.userType == userType) {
            return; // değişiklik yok → updatedAt dokunma
        }

        this.userType = userType;
        this.updatedAt = Instant.now();
    }
}
