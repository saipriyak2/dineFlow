package com.example.restaurant.repository;

import com.example.restaurant.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findByUserIdOrderByDateDesc(Long userId);

    Optional<Reservation> findByIdAndUserId(Long id, Long userId);

    @org.springframework.data.jpa.repository.Query(
            "SELECT COALESCE(SUM(r.partySize), 0) FROM Reservation r " +
                    "WHERE r.date = :date AND r.time = :time AND r.status <> com.example.restaurant.entity.enums.ReservationStatus.CANCELLED"

    )
    int sumPartySizeForSlot(LocalDate date, LocalTime time);
}










