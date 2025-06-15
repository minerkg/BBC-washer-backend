package com.bcc.washer.dto;

import com.bcc.washer.domain.reservation.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationDtoConverter extends BaseConverter<Reservation, ReservationDto> {

    @Autowired
    private UserDtoConverter userDtoConverter;

    @Autowired
    private BookableUnitDtoConverter bookableUnitDtoConverter;

    @Override
    Reservation convertDtoToModel(ReservationDto dto) {
        return null;
    }

    @Override
    public ReservationDto convertModelToDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .user(userDtoConverter.convertModelToDto(reservation.getUser()))
                .bookableUnit(bookableUnitDtoConverter.convertModelToDto(reservation.getBookableUnit()))
                .status(reservation.getStatus())
                .build();
    }
}
