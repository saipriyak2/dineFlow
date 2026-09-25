package com.example.restaurant.Controller;

import com.example.restaurant.Services.OrderService;
import com.example.restaurant.dto.common.PageResponse;
import com.example.restaurant.dto.request.CheckoutRequest;
import com.example.restaurant.dto.request.UpdateOrderStatusRequest;
import com.example.restaurant.dto.response.OrderResponse;
import com.example.restaurant.dto.response.OrderSummaryResponse;
import com.example.restaurant.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/api/orders/checkout")
    public ResponseEntity<OrderResponse> checkout(@AuthenticationPrincipal User currentUser,
                                                  @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.checkout(currentUser.getId(), request));
    }

    @GetMapping("/api/orders/{orderId}")
    public ResponseEntity<OrderResponse> getById(@AuthenticationPrincipal User currentUser, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getById(orderId, currentUser.getId()));
    }

    @GetMapping("/api/orders")
    public ResponseEntity<PageResponse<OrderSummaryResponse>> getMyOrders(
            @AuthenticationPrincipal User currentUser, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersForUser(currentUser.getId(), pageable));
    }

    @PostMapping("/api/orders/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@AuthenticationPrincipal User currentUser, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId, currentUser.getId()));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/api/orders/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long orderId,
                                                      @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, request));
    }
}
