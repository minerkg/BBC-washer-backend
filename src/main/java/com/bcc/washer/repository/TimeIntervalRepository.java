package com.bcc.washer.repository;

import com.bcc.washer.domain.time.TimeInterval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeIntervalRepository extends JpaRepository<TimeInterval, Long> {
}
