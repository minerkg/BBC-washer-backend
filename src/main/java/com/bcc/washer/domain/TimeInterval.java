package com.bcc.washer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class TimeInterval {

    @Id
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate date;

}
