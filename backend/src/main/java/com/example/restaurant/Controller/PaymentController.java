package com.example.restaurant.Controller;

import com.example.restaurant.Services.OrderService;
import com.example.restaurant.Services.PaymentService;
import com.example.restaurant.dto.request.PaymentRequest;
import com.example.restaurant.dto.response.PaymentResponse;
import com.example.restaurant.entity.User;
import com.example.restaurant.entity.enums.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;


    @PostMapping("/api/payments")
    public ResponseEntity<PaymentResponse> processPayment(@AuthenticationPrincipal User currentUser,
                                                          @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.processPayment(currentUser.getId(), request));
    }


    @GetMapping("/api/orders/{orderId}/payment")
    public ResponseEntity<PaymentResponse> getByOrderId(@AuthenticationPrincipal User currentUser,
                                                        @PathVariable Long orderId) {
        if (currentUser.getRole() != Role.ADMIN) {
            orderService.getById(orderId, currentUser.getId());
        }
        return ResponseEntity.ok(paymentService.getByOrderId(orderId));
    }
}
