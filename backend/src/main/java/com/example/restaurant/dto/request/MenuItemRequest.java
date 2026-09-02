package com.example.restaurant.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItemRequest(

        @NotBlank(message = "Item name is required")
        @Size(max = 150, message = "Item name must be at most 150 characters")
        String name,

        @Size(max = 1000, message = "Description must be at most 1000 characters")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,

        String imageUrl,



        @NotNull(message = "Category id is required")
        Long categoryId
) {
}