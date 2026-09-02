package com.example.restaurant.dto.request;

import jakarta.validation.constraints.NotNull;

public record MenuItemAvailabilityRequest(
        @NotNull(message = "Availability flag is required")
        Boolean available
) {
}