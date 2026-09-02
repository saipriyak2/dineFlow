package com.example.restaurant.mapper;

import com.example.restaurant.dto.response.ReviewResponse;
import com.example.restaurant.entity.Review;

public final class ReviewMapper {

    private ReviewMapper() {
    }

    public static ReviewResponse toResponse(Review review) {
        if (review == null) {
            return null;
        }
        return new ReviewResponse(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getName(),
                review.getOrder().getId(),
                review.getRating(),
                review.getText(),
                review.getCreatedAt()
        );
    }
}
