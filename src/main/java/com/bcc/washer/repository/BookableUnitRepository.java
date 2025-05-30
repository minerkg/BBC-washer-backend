package com.bcc.washer.repository;

import com.bcc.washer.domain.BookableUnit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookableUnitRepository extends JpaRepository<BookableUnit, Long> {

    @Override
    @Query("select b from BookableUnit b")
    @EntityGraph(value = "BookableUnit.withAllData")
    List<BookableUnit> findAll();
}
