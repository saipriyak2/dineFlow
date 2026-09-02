package com.example.restaurant.dto.response;

import com.example.restaurant.entity.enums.FulfillmentMethod;
import com.example.restaurant.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderSummaryResponse(
        Long id,
        OrderStatus status,
        FulfillmentMethod fulfillmentMethod,
        BigDecimal totalAmount,
        boolean paid,
        Instant createdAt,
        int itemCount
) {
}
