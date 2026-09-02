package com.example.restaurant.dto.response;

import com.example.restaurant.entity.enums.FulfillmentMethod;
import com.example.restaurant.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        OrderStatus status,
        FulfillmentMethod fulfillmentMethod,
        BigDecimal totalAmount,
        boolean paid,
        Instant createdAt,
        List<OrderItemResponse> items
) {
}
