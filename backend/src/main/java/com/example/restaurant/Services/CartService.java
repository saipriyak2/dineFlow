package com.example.restaurant.Services;
import com.example.restaurant.dto.request.AddCartItemRequest;
import com.example.restaurant.dto.request.UpdateCartItemRequest;
import com.example.restaurant.dto.response.CartResponse;

public interface CartService {

    CartResponse getCartForUser(Long userId);
    CartResponse addItem(Long userId,AddCartItemRequest request);
    CartResponse updateItemQuantity(Long userId,Long cartItemId,UpdateCartItemRequest request);
    CartResponse removeItem(Long userId,Long cartItemId);
    void clearCart(Long userId);
}
