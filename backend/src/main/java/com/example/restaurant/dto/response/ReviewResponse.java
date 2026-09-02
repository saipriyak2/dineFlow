package com.example.restaurant.dto.response;

import java.time.Instant;

public record ReviewResponse(
        Long id,
        Long userId,
        String userName,
        Long orderId,
        int rating,
        String text,
        Instant createdAt
) {
}
