package com.example.restaurant.mapper;
import com.example.restaurant.dto.response.CartItemResponse;
import com.example.restaurant.dto.response.CartResponse;
import com.example.restaurant.entity.Cart;
import com.example.restaurant.entity.CartItem;

import java.math.BigDecimal;
import java.util.List;

public final class CartMapper {

    private CartMapper() {
    }

    public static CartItemResponse toResponse(CartItem item) {
        if (item == null) {
            return null;
        }
        BigDecimal unitPrice = item.getMenuItem().getPrice();
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        return new CartItemResponse(
                item.getId(),
                item.getMenuItem().getId(),
                item.getMenuItem().getName(),
                unitPrice,
                item.getQuantity(),
                subtotal
        );
    }

    public static CartResponse toResponse(Cart cart) {
        if (cart == null) {
            return null;
        }
        List<CartItemResponse> items = cart.getItems().stream()
                .map(CartMapper::toResponse)
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                items,
                total
        );
    }
}

