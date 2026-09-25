package com.example.restaurant.controller;

import com.example.restaurant.Services.CartService;
import com.example.restaurant.dto.request.AddCartItemRequest;
import com.example.restaurant.dto.request.UpdateCartItemRequest;
import com.example.restaurant.dto.response.CartResponse;
import com.example.restaurant.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(cartService.getCartForUser(currentUser.getId()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@AuthenticationPrincipal User currentUser,
                                                @Valid @RequestBody AddCartItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(currentUser.getId(), request));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateItemQuantity(@AuthenticationPrincipal User currentUser,
                                                           @PathVariable Long cartItemId,
                                                           @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateItemQuantity(currentUser.getId(), cartItemId, request));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeItem(@AuthenticationPrincipal User currentUser,
                                                   @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeItem(currentUser.getId(), cartItemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User currentUser) {
        cartService.clearCart(currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
