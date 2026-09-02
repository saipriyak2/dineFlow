package com.example.restaurant.Services.Impl;
import com.example.restaurant.Services.ReservationService;
import com.example.restaurant.dto.request.ReservationRequest;
import com.example.restaurant.dto.request.ReservationStatusUpdateRequest;
import com.example.restaurant.dto.response.ReservationResponse;
import com.example.restaurant.entity.Reservation;
import com.example.restaurant.entity.User;
import com.example.restaurant.entity.enums.ReservationStatus;
import com.example.restaurant.exception.InvalidOperationException;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.mapper.ReservationMapper;
import com.example.restaurant.repository.ReservationRepository;
import com.example.restaurant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.dnd.InvalidDnDOperationException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final int SLOT_CAPACITY = 50;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReservationResponse create(Long userId,ReservationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.of("User",userId));

        int alreadyBooked = reservationRepository.sumPartySizeForSlot(request.date(),request.time());
        if(alreadyBooked + request.partySize() > SLOT_CAPACITY) {
            throw new InvalidOperationException(
                    "The" + request.date() + " " + request.time() + "slot doesn't have room for a party of"
                            + request.partySize());
        }

        Reservation reservation = Reservation.builder()
                .user(user)
                .reservationNumber("RES-"+ UUID.randomUUID().toString().substring(0,8).toUpperCase())
                .date(request.date())
                .time(request.time())
                .partySize(request.partySize())
                .status(ReservationStatus.UPCOMING)
                .build();

        return ReservationMapper.toResponse(reservationRepository.save(reservation));


    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getById(Long reservationId,Long userId){
        Reservation reservation = reservationRepository.findByIdAndUserId(reservationId,userId)
                .orElseThrow(() -> ResourceNotFoundException.of("Reservation",reservationId));
        return ReservationMapper.toResponse(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getForUser(Long userId) {
        return reservationRepository.findByUserIdOrderByDateDesc(userId).stream()
                .map(ReservationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ReservationResponse cancel(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findByIdAndUserId(reservationId,userId)
                .orElseThrow(() -> ResourceNotFoundException.of("Reservation",reservationId));

        if(reservation.getStatus() != ReservationStatus.UPCOMING) {
            throw new InvalidDnDOperationException("Reservation"+reservationId+"cannot be cancelled (status: "+reservation.getStatus()+")");

        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        return ReservationMapper.toResponse(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationResponse updateStatus(Long reservationId,Long userid,ReservationStatusUpdateRequest request){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> ResourceNotFoundException.of("Reservation",reservationId));

        reservation.setStatus(request.status());

        return ReservationMapper.toResponse(reservationRepository.save(reservation));
    }
}
