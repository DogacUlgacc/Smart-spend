package com.dogac.cart_service.infrastructure.mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;

import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.cart.CartId;
import com.dogac.cart_service.domain.cart.CartItem;
import com.dogac.cart_service.domain.enums.Currency;
import com.dogac.cart_service.domain.valueobjects.UserId;
import com.dogac.cart_service.infrastructure.entities.JpaCartEntity;
import com.dogac.cart_service.infrastructure.entities.JpaCartItemEntity;

@Mapper(componentModel = "spring", uses = { CartItemMapper.class })
public interface CartMapper {

    // JPA Entity -> Domain (rehydrate)
    default Cart toDomain(JpaCartEntity entity) {
        if (entity == null) {
            return null;
        }

        return Cart.rehydrate(
                new CartId(entity.getId()),
                new UserId(entity.getUserId()),
                entity.getCurrency(),
                toCartItemDomains(entity.getItems(), entity.getCurrency()));
    }

    // Domain -> JPA Entity
    default JpaCartEntity toEntity(Cart cart) {
        if (cart == null) {
            return null;
        }

        JpaCartEntity entity = JpaCartEntity.create();
        entity.setId(cart.getId().value());
        entity.setUserId(cart.getUserId().value());
        entity.setCurrency(cart.getCurrency());

        List<JpaCartItemEntity> itemEntities = toCartItemEntities(cart.getItems());
        itemEntities.forEach(item -> item.setCart(entity));
        entity.setItems(itemEntities);

        return entity;
    }

    List<CartItem> toCartItemDomains(List<JpaCartItemEntity> entities, @Context Currency currency);

    List<JpaCartItemEntity> toCartItemEntities(List<CartItem> items);
}
