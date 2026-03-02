package com.dogac.cart_service.infrastructure.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

import com.dogac.cart_service.domain.cart.CartItem;
import com.dogac.cart_service.domain.cart.CartItemId;
import com.dogac.cart_service.domain.enums.Currency;
import com.dogac.cart_service.domain.valueobjects.Money;
import com.dogac.cart_service.domain.valueobjects.ProductId;
import com.dogac.cart_service.domain.valueobjects.Quantity;
import com.dogac.cart_service.infrastructure.entities.JpaCartItemEntity;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    default CartItem toDomain(JpaCartItemEntity entity, @Context Currency currency) {
        if (entity == null) {
            return null;
        }

        return CartItem.rehydrate(
                new CartItemId(entity.getId()),
                new ProductId(entity.getProductId()),
                new Quantity(entity.getQuantity()),
                new Money(entity.getPrice(), currency));
    }

    @ObjectFactory
    default JpaCartItemEntity newEntity(CartItem item) {
        return JpaCartItemEntity.create();
    }

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "id", expression = "java(item.getId().value())")
    @Mapping(target = "productId", expression = "java(item.getProductId().value())")
    @Mapping(target = "quantity", expression = "java(item.getQuantity().value())")
    @Mapping(target = "price", expression = "java(item.getPrice().amount())")
    JpaCartItemEntity toEntity(CartItem item);
}
