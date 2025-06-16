package com.bcc.washer.dto;

import com.bcc.washer.domain.BookableUnit;
import org.springframework.stereotype.Service;

@Service
public class BookableUnitDtoConverter extends BaseConverter<BookableUnit, BookableUnitDto> {
    @Override
    BookableUnit convertDtoToModel(BookableUnitDto dto) {
        return null;
    }

    @Override
    public BookableUnitDto convertModelToDto(BookableUnit bookableUnit) {
        return BookableUnitDto.builder()
                .id(bookableUnit.getId())
                .isAvailable(bookableUnit.isAvailable())
                .timeSlot(bookableUnit.getTimeSlot())
                .washer(bookableUnit.getWasher())
                .build();
    }
}
