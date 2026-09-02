package com.example.restaurant.dto.response;

import com.example.restaurant.entity.enums.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(
        Long id,
        String reservationNumber,
        LocalDate date,
        LocalTime time,
        int partySize,
        ReservationStatus status
) {
}