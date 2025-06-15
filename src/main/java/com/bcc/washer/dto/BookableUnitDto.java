package com.bcc.washer.dto;

import com.bcc.washer.domain.time.TimeSlot;
import com.bcc.washer.domain.washer.Washer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookableUnitDto {

    private Long id;
    private Washer washer;
    private TimeSlot timeSlot;
    private boolean isAvailable;
}

