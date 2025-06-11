package com.bcc.washer.repository;

import com.bcc.washer.domain.BookableUnit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookableUnitRepository extends JpaRepository<BookableUnit, Long> {

    @Override
    @Query("select b from BookableUnit b")
    @EntityGraph(value = "BookableUnit.withAllData")
    List<BookableUnit> findAll();

    @Query("SELECT b FROM BookableUnit b JOIN b.timeSlot ts JOIN ts.timeInterval ti " +
            "WHERE b.isAvailable = true AND ti.date >= :startDate AND ti.date < :endDate")
    @EntityGraph(value = "BookableUnit.withAllData")
    List<BookableUnit> findAllAvailableWithinDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT b FROM BookableUnit b " +
            "JOIN b.timeSlot ts JOIN ts.timeInterval ti " +
            "JOIN b.washer w " +
            "WHERE w.id = :washerId AND ti.date = :date")
    List<BookableUnit> findByWasherIdAndDate(@Param("washerId") Long washerId, @Param("date") LocalDate date);
}
