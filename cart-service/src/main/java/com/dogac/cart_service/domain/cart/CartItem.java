package com.dogac.cart_service.domain.cart;

import java.util.Objects;

import com.dogac.cart_service.domain.valueobjects.Money;
import com.dogac.cart_service.domain.valueobjects.ProductId;
import com.dogac.cart_service.domain.valueobjects.Quantity;

public class CartItem {

    private final CartItemId id;
    private final ProductId productId;
    private Quantity quantity;
    private Money price;

    private CartItem(
            CartItemId id,
            ProductId productId,
            Quantity quantity,
            Money price) {
        this.id = Objects.requireNonNull(id, "CartItem id cannot be null");
        this.productId = Objects.requireNonNull(productId, "ProductId cannot be null");
        this.quantity = Objects.requireNonNull(quantity, "Quantity cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");
    }

    public static CartItem create(ProductId productId, Quantity quantity, Money price) {
        return new CartItem(CartItemId.generate(), productId, quantity, price);
    }

    public static CartItem rehydrate(CartItemId id, ProductId productId, Quantity quantity, Money price) {
        return new CartItem(id, productId, quantity, price);
    }

    public CartItemId getId() {
        return id;
    }

    public ProductId getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    void increaseQuantity(Quantity delta) {
        Objects.requireNonNull(delta, "Delta quantity cannot be null");
        this.quantity = this.quantity.add(delta);
    }

    void changeQuantity(Quantity newQuantity) {
        this.quantity = Objects.requireNonNull(newQuantity, "New quantity cannot be null");
    }

    void changePrice(Money newPrice) {
        this.price = Objects.requireNonNull(newPrice, "New price cannot be null");
    }

    public Money totalPrice() {
        return price.multiply(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CartItem other))
            return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
