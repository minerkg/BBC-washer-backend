package com.bcc.washer.repository;

import com.bcc.washer.domain.BookableUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookableUnitRepository extends JpaRepository<BookableUnit, Long> {
}
