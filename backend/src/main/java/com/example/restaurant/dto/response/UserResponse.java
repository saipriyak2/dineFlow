package com.example.restaurant.dto.response;

import com.example.restaurant.entity.enums.Role;

import java.time.Instant;

public record UserResponse(
        Long id,
        String name,
        String email,
        Role role,
        Instant createdAt
) {
}
