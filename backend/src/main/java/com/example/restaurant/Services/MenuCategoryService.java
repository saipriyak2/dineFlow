package com.example.restaurant.Services;
import com.example.restaurant.dto.request.MenuCategoryRequest;
import com.example.restaurant.dto.request.MenuItemAvailabilityRequest;
import com.example.restaurant.dto.response.MenuCategoryResponse;
import com.example.restaurant.dto.response.MenuItemResponse;

import java.util.List;
public interface MenuCategoryService {

    MenuCategoryResponse create(MenuCategoryRequest request);
    MenuCategoryResponse update(Long categoryId, MenuCategoryRequest request);
    void delete(Long categoryId);
    MenuCategoryResponse getById(Long categoryId);
    List<MenuCategoryResponse> getAll();
}
