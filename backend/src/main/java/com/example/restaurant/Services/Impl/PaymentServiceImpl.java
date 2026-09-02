package com.example.restaurant.Services.Impl;
import com.example.restaurant.Services.PaymentService;
import com.example.restaurant.dto.request.PaymentRequest;
import com.example.restaurant.dto.response.PaymentResponse;
import com.example.restaurant.entity.Order;
import com.example.restaurant.entity.Payment;
import com.example.restaurant.entity.enums.PaymentStatus;
import com.example.restaurant.exception.DuplicateResourceException;
import com.example.restaurant.exception.InvalidOperationException;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.mapper.PaymentMapper;
import com.example.restaurant.repository.OrderRepository;
import com.example.restaurant.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentResponse processPayment(Long userId,PaymentRequest request) {
        Order order = orderRepository.findByIdAndUserId(request.orderId(), userId)
                .orElseThrow(() -> ResourceNotFoundException.of("Order", request.orderId()));

        if (order.isPaid()) {
            throw new InvalidOperationException("Order" + order.getId() + "has already been paid");
        }
        if (paymentRepository.findByOrderId(order.getId()).isPresent()) {
            throw new DuplicateResourceException("A payment already exists for order" + order.getId());

        }

        Payment payment = Payment.builder()
                .order(order)
                .method(request.method())
                .status(PaymentStatus.SUCCESS)
                .amount(order.getTotalAmount())
                .receiptNumber("RCPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();

        Payment saved = paymentRepository.save(payment);

        order.setPaid(true);
        orderRepository.save(order);

        return PaymentMapper.toResponse(saved);
    }
    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getByOrderId(Long orderId){
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("No payment found for order"+orderId));
        return PaymentMapper.toResponse(payment);
    }
}
