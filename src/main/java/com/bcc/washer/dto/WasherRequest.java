package com.bcc.washer.dto;

import com.bcc.washer.domain.washer.WasherStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WasherRequest {


    private String name;

    private int capacity;

    @Enumerated(EnumType.STRING)
    private WasherStatus status;

}
