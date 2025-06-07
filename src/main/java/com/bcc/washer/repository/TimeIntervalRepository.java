package com.bcc.washer.repository;

import com.bcc.washer.domain.TimeInterval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeIntervalRepository  extends JpaRepository<TimeInterval, Long> {
}
