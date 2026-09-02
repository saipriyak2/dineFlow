package com.example.restaurant.Services;
import com.example.restaurant.dto.common.PageResponse;
import com.example.restaurant.dto.request.MenuItemAvailabilityRequest;
import com.example.restaurant.dto.request.MenuItemRequest;
import com.example.restaurant.dto.response.MenuItemResponse;
import org.springframework.data.domain.Pageable;

public interface MenuItemService {

    MenuItemResponse create(MenuItemRequest request);

    MenuItemResponse update(Long itemId, MenuItemRequest request);
    void delete(Long itemId);
    MenuItemResponse getById(Long itemId);

    PageResponse<MenuItemResponse> search(String name,Long categoryId,Boolean available,Pageable pageable);
    MenuItemResponse setAvailability(Long itemId, MenuItemAvailabilityRequest request);
}
