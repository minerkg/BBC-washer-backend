package com.bcc.washer.dto;


import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class BetweenDatesRequest {

    private LocalDate startDate;
    private LocalDate endDate;
}
