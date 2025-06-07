package com.bcc.washer.repository;

import com.bcc.washer.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Override
    @Query("select r from Reservation r")
    @EntityGraph(value = "Reservation.withAllData")
    List<Reservation> findAll();
}
