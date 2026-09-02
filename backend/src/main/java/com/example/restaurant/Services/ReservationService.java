package com.example.restaurant.Services;

import com.example.restaurant.dto.request.ReservationRequest;
import com.example.restaurant.dto.request.ReservationStatusUpdateRequest;
import com.example.restaurant.dto.response.ReservationResponse;

import java.util.List;

public interface ReservationService {

    ReservationResponse create(Long userId, ReservationRequest request);
    ReservationResponse getById(Long reservationId, Long userId);
    List<ReservationResponse> getForUser(Long userId);
    ReservationResponse cancel( Long reservationId,Long userId);
    ReservationResponse updateStatus(Long reservationId, Long userId, ReservationStatusUpdateRequest request);

}
