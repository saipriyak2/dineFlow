package com.example.restaurant.dto.request;

import com.example.restaurant.entity.enums.FulfillmentMethod;
import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(
        @NotNull(message = "Fulfillment method is required")
        FulfillmentMethod fulfillmentMethod
) {
}