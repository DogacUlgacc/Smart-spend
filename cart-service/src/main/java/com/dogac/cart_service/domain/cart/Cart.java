package com.dogac.cart_service.domain.cart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dogac.cart_service.domain.core.AggregateRoot;
import com.dogac.cart_service.domain.enums.Currency;
import com.dogac.cart_service.domain.valueobjects.Money;
import com.dogac.cart_service.domain.valueobjects.ProductId;
import com.dogac.cart_service.domain.valueobjects.Quantity;
import com.dogac.cart_service.domain.valueobjects.UserId;

public class Cart implements AggregateRoot<CartId> {

    private final CartId id;
    private final UserId userId;
    private final Currency currency;
    private final List<CartItem> items;

    private Cart(CartId id, UserId userId, Currency currency, List<CartItem> items) {
        this.id = Objects.requireNonNull(id, "Cart id cannot be null");
        this.userId = Objects.requireNonNull(userId, "UserId cannot be null");
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    public static Cart create(UserId userId, Currency currency) {
        return new Cart(CartId.generate(), userId, currency, new ArrayList<>());
    }

    public static Cart rehydrate(CartId id, UserId userId, Currency currency, List<CartItem> items) {
        Cart cart = new Cart(id, userId, currency, items);
        cart.validateInvariant();
        return cart;
    }

    @Override
    public CartId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void addItem(ProductId productId, Quantity quantity, Money price) {
        Objects.requireNonNull(productId, "Product Id cannot be null");
        Objects.requireNonNull(quantity, "Quantity cannot be null");
        Objects.requireNonNull(price, "Price cannot be null");

        if (price.currency() != this.currency) {
            throw new IllegalArgumentException(
                    "Item currency must match cart currency");
        }

        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().increaseQuantity(quantity);
            existingItem.get().changePrice(price);
        } else {
            items.add(CartItem.create(productId, quantity, price));
        }
    }

    public void removeItem(ProductId productId) {
        Objects.requireNonNull(productId, "ProductId cannot be null");

        CartItem item = items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cart item not found for productId=" + productId));

        items.remove(item);

        totalAmount();
    }

    public void changeItemQuantity(ProductId productId, Quantity newQuantity) {
        Objects.requireNonNull(productId, "ProductId cannot be null");
        Objects.requireNonNull(newQuantity, "New quantity cannot be null");

        CartItem item = findItemByProductId(productId)
                .orElseThrow(() -> new IllegalStateException("Cart item not found for productId=" + productId));
        item.changeQuantity(newQuantity);
    }

    public void clear() {
        items.clear();
    }

    public Money totalAmount() {
        return items.stream()
                .map(CartItem::totalPrice)
                .reduce(Money.zero(currency), Money::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private Optional<CartItem> findItemByProductId(ProductId productId) {
        return items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();
    }

    private void validateInvariant() {
        for (CartItem item : items) {
            if (item.getPrice().currency() != this.currency) {
                throw new IllegalStateException("Cart item currency must match cart currency");
            }
        }
        ensureNoDuplicateProducts();
    }

    public boolean containsProduct(ProductId productId) {
        return items.stream()
                .anyMatch(item -> item.getProductId().equals(productId));
    }

    private void ensureNoDuplicateProducts() {
        long distinctCount = items.stream().map(CartItem::getProductId).distinct().count();

        if (distinctCount != items.size()) {
            throw new IllegalStateException(
                    "Cart invariant violated: duplicate ProductId found");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Cart other))
            return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
