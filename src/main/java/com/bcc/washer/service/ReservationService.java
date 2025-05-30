package com.bcc.washer.service;


import com.bcc.washer.domain.Reservation;
import com.bcc.washer.repository.BookableUnitRepository;
import com.bcc.washer.repository.ReservationRepository;
import com.bcc.washer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private BookableUnitRepository bookableUnitRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    public Reservation makeReservation(Long bookableUnitId, Long userId) {
        var bookableUnit = bookableUnitRepository.findById(bookableUnitId).orElseThrow(
                () -> new RuntimeException("unit not found")
        );
        var user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("user not found")
        );
        var newReservation = Reservation.builder().user(user).bookableUnit(bookableUnit).build();
        reservationRepository.save(Reservation.builder().user(user).bookableUnit(bookableUnit).build());
        return newReservation;

    }

    public Set<Reservation> findAllByUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("user not found")
        );
        return reservationRepository.findAll().stream().filter(
                reservation -> reservation.getUser().equals(user)
        ).collect(Collectors.toSet());
    }
}
