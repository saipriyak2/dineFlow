package com.example.restaurant.Services;

import com.example.restaurant.dto.common.PageResponse;
import com.example.restaurant.dto.request.ReviewRequest;
import com.example.restaurant.dto.response.ReviewResponse;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    ReviewResponse create(Long userId, ReviewRequest request);
    ReviewResponse getById(Long reviewId);
    PageResponse<ReviewResponse> getAll(Pageable pageable);
}
