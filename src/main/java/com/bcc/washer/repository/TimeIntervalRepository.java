package com.bcc.washer.repository;

import com.bcc.washer.domain.time.TimeInterval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.Date;

public interface TimeIntervalRepository extends JpaRepository<TimeInterval, Long> {

    public TimeInterval findByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);
}
