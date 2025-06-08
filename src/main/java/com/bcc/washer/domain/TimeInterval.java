package com.bcc.washer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeInterval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;

    @JsonIgnore
    @OneToOne(mappedBy = "timeInterval", cascade = CascadeType.ALL)
    private TimeSlot timeSlot;


    public boolean equals(TimeInterval that) {
        if (this == that) return true;
        if (that == null) return false;
        return this.startTime.equals(that.startTime) && this.endTime.equals(that.endTime);
    }

}
