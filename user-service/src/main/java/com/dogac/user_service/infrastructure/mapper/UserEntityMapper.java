package com.dogac.user_service.infrastructure.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dogac.user_service.domain.entities.User;
import com.dogac.user_service.domain.valueobjects.Address;
import com.dogac.user_service.domain.valueobjects.Email;
import com.dogac.user_service.domain.valueobjects.ExternalId;
import com.dogac.user_service.domain.valueobjects.FullName;
import com.dogac.user_service.domain.valueobjects.PhoneNumber;
import com.dogac.user_service.domain.valueobjects.UserId;
import com.dogac.user_service.infrastructure.entities.AddressEmbeddable;
import com.dogac.user_service.infrastructure.entities.JpaUserEntity;

@Component
public class UserEntityMapper {

        public JpaUserEntity toEntity(User domain) {
                JpaUserEntity entity = new JpaUserEntity();
                entity.setId(domain.getId().value());
                entity.setExternalId(domain.getExternalId() != null ? domain.getExternalId().value() : null);
                entity.setFirstName(domain.getFullName().firstName());
                entity.setLastName(domain.getFullName().lastName());
                entity.setEmail(domain.getEmail().value());
                entity.setPhoneNumber(domain.getPhoneNumber() != null ? domain.getPhoneNumber().value() : null);
                entity.setUserType(domain.getUserType());
                entity.setStatus(domain.getStatus());
                entity.setAddresses(domain.getAddresses().stream()
                                .map(a -> new AddressEmbeddable(a.title(), a.city(), a.street(), a.country()))
                                .toList());
                entity.setCreatedAt(domain.getCreatedAt());
                entity.setUpdatedAt(domain.getUpdatedAt());
                return entity;
        }

        public User toDomain(JpaUserEntity entity) {
                List<Address> addresses = entity.getAddresses() != null
                                ? entity.getAddresses().stream()
                                                .map(a -> new Address(a.getTitle(), a.getCity(), a.getStreet(),
                                                                a.getCountry()))
                                                .toList()
                                : List.of();

                return User.rehydrate(
                                UserId.from(entity.getId()),
                                entity.getExternalId() != null ? new ExternalId(entity.getExternalId()) : null,
                                new FullName(entity.getFirstName(), entity.getLastName()),
                                new Email(entity.getEmail()),
                                entity.getPhoneNumber() != null ? new PhoneNumber(entity.getPhoneNumber()) : null,
                                entity.getUserType(),
                                entity.getStatus(),
                                addresses,
                                entity.getCreatedAt(),
                                entity.getUpdatedAt());
        }
}
