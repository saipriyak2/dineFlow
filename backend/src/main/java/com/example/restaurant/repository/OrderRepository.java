package com.example.restaurant.repository;

import com.example.restaurant.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByUserId(Long userId, Pageable pageable);

    java.util.Optional<Order> findByIdAndUserId(Long id,Long userId);
}
