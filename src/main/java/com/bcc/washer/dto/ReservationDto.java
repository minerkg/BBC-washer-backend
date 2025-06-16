package com.bcc.washer.dto;

import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.reservation.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReservationDto {
    private Long id;
    private UserDto user;
    private BookableUnitDto bookableUnit;
    private ReservationStatus status;
}
