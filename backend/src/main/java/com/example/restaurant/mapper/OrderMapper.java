package com.example.restaurant.mapper;

import com.example.restaurant.dto.response.OrderItemResponse;
import com.example.restaurant.dto.response.OrderResponse;
import com.example.restaurant.dto.response.OrderSummaryResponse;
import com.example.restaurant.entity.Order;
import com.example.restaurant.entity.OrderItem;

import java.math.BigDecimal;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderItemResponse toResponse(OrderItem item) {
        if (item == null) {
            return null;
        }
        BigDecimal subtotal = item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity()));
        return new OrderItemResponse(
                item.getId(),
                item.getMenuItem() != null ? item.getMenuItem().getId() : null,
                item.getNameAtOrder(),
                item.getPriceAtOrder(),
                item.getQuantity(),
                subtotal
        );
    }

    public static OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }
        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                order.getFulfillmentMethod(),
                order.getTotalAmount(),
                order.isPaid(),
                order.getCreatedAt(),
                order.getItems().stream().map(OrderMapper::toResponse).toList()
        );
    }

    public static OrderSummaryResponse toSummaryResponse(Order order) {
        if (order == null) {
            return null;
        }
        return new OrderSummaryResponse(
                order.getId(),
                order.getStatus(),
                order.getFulfillmentMethod(),
                order.getTotalAmount(),
                order.isPaid(),
                order.getCreatedAt(),
                order.getItems() == null ? 0 : order.getItems().size()
        );
    }
}

