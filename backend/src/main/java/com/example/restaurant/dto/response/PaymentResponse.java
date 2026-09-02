package com.example.restaurant.dto.response;

import com.example.restaurant.entity.enums.PaymentMethod;
import com.example.restaurant.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        Long id,
        Long orderId,
        PaymentMethod method,
        PaymentStatus status,
        BigDecimal amount,
        String receiptNumber,
        Instant createdAt
) {
}