package com.bcc.washer.dto;

import com.bcc.washer.domain.washer.WasherStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class BookableUnitDTO implements Serializable {
    private Long id;
    private Long washerId;
    private String washerName;
    private double washerCapacity;
    private WasherStatus washerStatus;
    private Long timeSlotId;
    private LocalDate timeIntervalDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isAvailable;

    public BookableUnitDTO(Long id, Long washerId, String washerName, double washerCapacity, WasherStatus washerStatus,
                           Long timeSlotId, LocalDate timeIntervalDate, LocalTime startTime, LocalTime endTime, boolean isAvailable) {
        this.id = id;
        this.washerId = washerId;
        this.washerName = washerName;
        this.washerCapacity = washerCapacity;
        this.washerStatus = washerStatus;
        this.timeSlotId = timeSlotId;
        this.timeIntervalDate = timeIntervalDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
    }


}