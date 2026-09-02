package com.example.restaurant.Services;
import com.example.restaurant.dto.request.PaymentRequest;
import com.example.restaurant.dto.response.PaymentResponse;
public interface PaymentService {
    PaymentResponse processPayment(Long userId,PaymentRequest request);
    PaymentResponse getByOrderId(Long orderId);
}
