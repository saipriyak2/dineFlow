package com.example.restaurant.mapper;

import com.example.restaurant.dto.response.ReservationResponse;
import com.example.restaurant.entity.Reservation;

public final class ReservationMapper {

    private ReservationMapper() {
    }

    public static ReservationResponse toResponse(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return new ReservationResponse(
                reservation.getId(),
                reservation.getReservationNumber(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getPartySize(),
                reservation.getStatus()
        );
    }
}
