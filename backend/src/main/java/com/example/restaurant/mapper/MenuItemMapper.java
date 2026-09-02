package com.example.restaurant.mapper;

import com.example.restaurant.dto.response.MenuItemResponse;
import com.example.restaurant.entity.MenuItem;

public final class MenuItemMapper {

    private MenuItemMapper() {
    }

    public static MenuItemResponse toResponse(MenuItem item) {
        if (item == null) {
            return null;
        }
        return new MenuItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getImageUrl(),
                item.isAvailable(),
                item.getCategory() != null ? item.getCategory().getId() : null,
                item.getCategory() != null ? item.getCategory().getName() : null
        );
    }
}
