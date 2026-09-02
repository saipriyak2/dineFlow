package com.example.restaurant.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRequest(

        @NotNull(message = "Order id is required")
        Long orderId,

        @Min(value = 1, message = "Rating must be between 1 and 5")
        @Max(value = 5, message = "Rating must be between 1 and 5")
        int rating,

        @Size(max = 2000, message = "Review text must be at most 2000 characters")
        String text
) {
}
