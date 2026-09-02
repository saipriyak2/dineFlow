package com.example.restaurant.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequest(

        @NotNull(message = "Date is required")
        @FutureOrPresent(message = "Date cannot be in the past")
        LocalDate date,

        @NotNull(message = "Time is required")
        LocalTime time,

        @Min(value = 1, message = "Party size must be at least 1")
        @Max(value = 20, message = "Party size must be at most 20; call us for larger groups")
        int partySize
) {
}