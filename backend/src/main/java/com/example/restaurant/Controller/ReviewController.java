package com.example.restaurant.Controller;

import com.example.restaurant.Services.ReviewService;
import com.example.restaurant.dto.common.PageResponse;
import com.example.restaurant.dto.request.ReviewRequest;
import com.example.restaurant.dto.response.ReviewResponse;
import com.example.restaurant.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    @PostMapping("/api/reviews")
    public ResponseEntity<ReviewResponse> create(@AuthenticationPrincipal User currentUser,
                                                 @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(currentUser.getId(), request));
    }

    @GetMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> getById(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getById(reviewId));
    }

    @GetMapping("/api/reviews")
    public ResponseEntity<PageResponse<ReviewResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getAll(pageable));
    }
}
