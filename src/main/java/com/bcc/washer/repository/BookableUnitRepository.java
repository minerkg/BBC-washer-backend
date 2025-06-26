package com.bcc.washer.repository;

import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.washer.Washer;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
public interface BookableUnitRepository extends JpaRepository<BookableUnit, Long> {

    @Override
    @Query("select b from BookableUnit b")
    @EntityGraph(value = "BookableUnit.withAllData")
    List<BookableUnit> findAll();

    @Query("select b from BookableUnit b where b.timeSlot.timeInterval.date = :localDate")
    @EntityGraph(value = "BookableUnit.withAllData")
    List<BookableUnit> findAllByDate(@Param("localDate") LocalDate localDate);

    @Query("select b from BookableUnit b where b.washer.id = :washerId AND b.timeSlot.timeInterval.date > :now")
    @EntityGraph(value = "BookableUnit.withAllData")
    List<BookableUnit> findAllByWasherAfterNow(@Param("washerId") Long washerId, @Param("now") LocalDate now);

    @Query("select b from BookableUnit b where b.timeSlot.id = :timeSlotId")
    @EntityGraph(value = "BookableUnit.withAllData")
    List<BookableUnit> findAllByTimeSlotId(Long timeSlotId);
}
