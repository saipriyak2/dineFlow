package com.example.restaurant.dto.response;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long menuItemId,
        String menuItemName,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal subtotal
) {
}
