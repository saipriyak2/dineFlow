package com.example.restaurant.Services;
import com.example.restaurant.dto.common.PageResponse;
import com.example.restaurant.dto.request.CheckoutRequest;
import com.example.restaurant.dto.request.UpdateOrderStatusRequest;
import com.example.restaurant.dto.response.OrderResponse;
import com.example.restaurant.dto.response.OrderSummaryResponse;
import com.example.restaurant.repository.OrderRepository;
import org.springframework.data.domain.Pageable;


public interface OrderService {
    OrderResponse checkout(Long userId,CheckoutRequest request);
    OrderResponse getById(Long orderId,Long userId);
    PageResponse<OrderSummaryResponse> getOrdersForUser(Long userId,Pageable pageable);
    OrderResponse updateStatus(Long orderId,UpdateOrderStatusRequest request);
    OrderResponse cancelOrder(Long orderId,Long userId);
}
