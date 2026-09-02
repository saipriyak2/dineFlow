package com.example.restaurant.mapper;

import com.example.restaurant.dto.response.MenuCategoryResponse;
import com.example.restaurant.entity.MenuCategory;

public final class MenuCategoryMapper {

    private MenuCategoryMapper() {
    }

    public static MenuCategoryResponse toResponse(MenuCategory category) {
        if (category == null) {
            return null;
        }
        int itemCount = category.getMenuItems() == null ? 0 : category.getMenuItems().size();
        return new MenuCategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                itemCount
        );
    }
}