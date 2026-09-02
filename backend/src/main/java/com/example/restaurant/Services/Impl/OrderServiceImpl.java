package com.example.restaurant.Services.Impl;
import com.example.restaurant.Services.OrderService;
import com.example.restaurant.dto.common.PageResponse;
import com.example.restaurant.dto.request.CheckoutRequest;
import com.example.restaurant.dto.request.UpdateOrderStatusRequest;
import com.example.restaurant.dto.response.OrderResponse;
import com.example.restaurant.dto.response.OrderSummaryResponse;
import com.example.restaurant.entity.Cart;
import com.example.restaurant.entity.Order;
import com.example.restaurant.entity.OrderItem;
import com.example.restaurant.entity.enums.OrderStatus;
import com.example.restaurant.exception.InvalidOperationException;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.mapper.OrderMapper;
import com.example.restaurant.repository.CartRepository;
import com.example.restaurant.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.dnd.InvalidDnDOperationException;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Set<OrderStatus> CANCELLABLE_STATUSES = EnumSet.of(OrderStatus.PENDING,OrderStatus.CONFIRMED);
    private static final Set<OrderStatus> TERMINAL_STATUSES = EnumSet.of(OrderStatus.COMPLETED,OrderStatus.CANCELLED);

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public OrderResponse checkout(Long userId,CheckoutRequest request){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new InvalidOperationException("Cart is empty;add items before checking out"));

        if(cart.getItems().isEmpty()){
            throw new InvalidOperationException("Cart is empty;add items before checking out");

        }

        Order order = Order.builder()
                .user(cart.getUser())
                .status(OrderStatus.PENDING)
                .fulfillmentMethod(request.fulfillmentMethod())
                .totalAmount(BigDecimal.ZERO)
                .paid(false)
                .build();

        cart.getItems().forEach(cartItem -> order.getItems().add(
                OrderItem.builder()
                        .order(order)
                        .menuItem(cartItem.getMenuItem())
                        .nameAtOrder(cartItem.getMenuItem().getName())
                        .priceAtOrder(cartItem.getMenuItem().getPrice())
                        .quantity(cartItem.getQuantity())
                        .build()

        ));

        BigDecimal total = order.getItems().stream()
                .map(item -> item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return OrderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(Long orderId,Long userId){
        Order order = orderRepository.findByIdAndUserId(orderId,userId)
                .orElseThrow(() -> ResourceNotFoundException.of("Order",orderId));
        return OrderMapper.toResponse(order);

    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderSummaryResponse> getOrdersForUser(Long userId,Pageable pageable){
        Page<Order> page = orderRepository.findByUserId(userId,pageable);
        return PageResponse.of(page,page.getContent().stream().map(OrderMapper::toSummaryResponse).toList());

    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Long orderId,UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ResourceNotFoundException.of("Order", orderId));

        if (TERMINAL_STATUSES.contains(order.getStatus())) {
            throw new InvalidOperationException(
                    "Order" + orderId + "is already" + order.getStatus() + "and cannot be updated further");
        }
        order.setStatus(request.status());
        return OrderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId,Long userId){
        Order order = orderRepository.findByIdAndUserId(orderId,userId)
                .orElseThrow(() -> ResourceNotFoundException.of("Order",orderId));

        if(!TERMINAL_STATUSES.contains(order.getStatus())) {
            throw new InvalidOperationException(
                    "Order" + orderId + "can no longer be cancelled (status:" + order.getStatus() + ")");
        }
        order.setStatus(OrderStatus.CANCELLED);
        return OrderMapper.toResponse(orderRepository.save(order));
        }
    }





