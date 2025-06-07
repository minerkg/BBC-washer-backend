package com.bcc.washer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeInterval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate date;

    @JsonIgnore
    @OneToOne(mappedBy = "timeInterval", cascade = CascadeType.ALL)
    private TimeSlot timeSlot;


}
