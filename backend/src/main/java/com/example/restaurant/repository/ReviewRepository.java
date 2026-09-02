package com.example.restaurant.repository;

import com.example.restaurant.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    boolean existsByOrderId(Long orderId);
}
