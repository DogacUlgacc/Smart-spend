package com.dogac.cart_service.infrastructure.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.dogac.cart_service.domain.enums.Currency;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "carts")
public class JpaCartEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, length = 10)
    private Currency currency;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaCartItemEntity> items = new ArrayList<>();

    protected JpaCartEntity() {
        // JPA only
    }

    // Public factory method for mapper
    public static JpaCartEntity create() {
        return new JpaCartEntity();
    }

    public void addItem(JpaCartItemEntity item) {
        items.add(item);
        item.setCart(this);
    }

    public void removeItem(JpaCartItemEntity item) {
        items.remove(item);
        item.setCart(null);
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public List<JpaCartItemEntity> getItems() {
        return items;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setItems(List<JpaCartItemEntity> items) {
        this.items.clear();
        for (JpaCartItemEntity item : items) {
            addItem(item);
        }
    }

    @PrePersist
    void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
