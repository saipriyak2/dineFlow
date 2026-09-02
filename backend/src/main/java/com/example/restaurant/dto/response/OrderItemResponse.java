package com.example.restaurant.dto.response;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long menuItemId,
        String nameAtOrder,
        BigDecimal priceAtOrder,
        int quantity,
        BigDecimal subtotal
) {
}
