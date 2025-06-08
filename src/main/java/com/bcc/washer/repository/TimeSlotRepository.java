package com.bcc.washer.repository;

import com.bcc.washer.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.timeInterval.date BETWEEN :from AND :to")
    List<TimeSlot> findAllByDateRange(@Param("from") LocalDate from, @Param("to") LocalDate to);

}
