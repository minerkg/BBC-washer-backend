package com.bcc.washer.dto;

import com.bcc.washer.domain.reservation.Reservation;
import org.springframework.beans.factory.annotation.Autowired;

public class ReservationDtoConverter extends BaseConverter<Reservation, ReservationDto> {

    @Autowired
    private UserDtoConverter userDtoConverter;

    @Override
    Reservation convertDtoToModel(ReservationDto dto) {
        return null;
    }

    @Override
    ReservationDto convertModelToDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .user(userDtoConverter.convertModelToDto(reservation.getUser()))
                .bookableUnit(reservation.getBookableUnit())
                .status(reservation.getStatus())
                .build();
    }
}
