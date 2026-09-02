package com.example.restaurant.mapper;

import com.example.restaurant.dto.response.PaymentResponse;
import com.example.restaurant.entity.Payment;

public final class PaymentMapper {

    private PaymentMapper() {
    }

    public static PaymentResponse toResponse(Payment payment) {
        if (payment == null) {
            return null;
        }
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getReceiptNumber(),
                payment.getCreatedAt()
        );
    }
}
