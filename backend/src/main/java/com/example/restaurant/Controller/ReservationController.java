package com.example.restaurant.Controller;

import com.example.restaurant.Services.ReservationService;
import com.example.restaurant.dto.request.ReservationRequest;
import com.example.restaurant.dto.request.ReservationStatusUpdateRequest;
import com.example.restaurant.dto.response.ReservationResponse;
import com.example.restaurant.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@AuthenticationPrincipal User currentUser,
                                                      @Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(currentUser.getId(), request));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getById(@AuthenticationPrincipal User currentUser,
                                                       @PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.getById(reservationId, currentUser.getId()));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getMyReservations(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(reservationService.getForUser(currentUser.getId()));
    }

    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<ReservationResponse> cancel(@AuthenticationPrincipal User currentUser,
                                                      @PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.cancel(reservationId, currentUser.getId()));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{reservationId}/status")
    public ResponseEntity<ReservationResponse> updateStatus(@AuthenticationPrincipal User currentUser,
                                                            @PathVariable Long reservationId,
                                                            @Valid @RequestBody ReservationStatusUpdateRequest request) {
        return ResponseEntity.ok(reservationService.updateStatus(reservationId, currentUser.getId(), request));
    }
}
