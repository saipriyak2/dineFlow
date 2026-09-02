package com.example.restaurant.Services.Impl;

import com.example.restaurant.Services.ReviewService;
import com.example.restaurant.dto.common.PageResponse;
import com.example.restaurant.dto.request.ReviewRequest;
import com.example.restaurant.dto.response.ReviewResponse;
import com.example.restaurant.entity.Order;
import com.example.restaurant.entity.Review;
import com.example.restaurant.entity.enums.OrderStatus;
import com.example.restaurant.exception.DuplicateResourceException;
import com.example.restaurant.exception.InvalidOperationException;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.mapper.ReviewMapper;
import com.example.restaurant.repository.OrderRepository;
import com.example.restaurant.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional

    public ReviewResponse create(Long userId,ReviewRequest request){
        Order order = orderRepository.findByIdAndUserId(request.orderId(),userId)
                .orElseThrow(() -> ResourceNotFoundException.of("Order",request.orderId()));

        if(order.getStatus() != OrderStatus.COMPLETED) {
            throw new InvalidOperationException("Only completed orders can be reviewed");
        }
        if(reviewRepository.existsByOrderId(order.getId())) {
            throw new DuplicateResourceException("Order" + order.getId()+"has already been reviewed");
        }

        Review review = Review.builder()
                .user(order.getUser())
                .order(order)
                .rating(request.rating())
                .text(request.text())
                .build();

        return ReviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getById(Long reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ResourceNotFoundException.of("Review",reviewId));
        return ReviewMapper.toResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> getAll(Pageable pageable){
        Page<Review> page = reviewRepository.findAll(pageable);
        return PageResponse.of(page,page.getContent().stream().map(ReviewMapper::toResponse).toList());
    }
}
