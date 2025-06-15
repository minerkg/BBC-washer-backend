package com.bcc.washer.dto;

import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.reservation.ReservationStatus;

public class ReservationDto {
    private Long id;
    private UserDto user;
    private BookableUnit bookableUnit;
    private ReservationStatus status;
}
