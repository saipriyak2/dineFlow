package com.example.restaurant.dto.request;

import com.example.restaurant.entity.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(

        @NotNull(message = "Order id is required")
        Long orderId,

        @NotNull(message = "Payment method is required")
        PaymentMethod method
) {
}