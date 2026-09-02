package com.example.restaurant.dto.response;

public record MenuCategoryResponse(
        Long id,
        String name,
        String description,
        int itemCount
) {
}
