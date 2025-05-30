package com.bcc.washer.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookableUnit {

    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Washer washer;

    @ManyToOne(cascade = CascadeType.ALL)
    private TimeSlot timeSlot;

    private boolean isAvailable;

    public BookableUnit(Washer washer, TimeSlot timeSlot) {
        this.washer = washer;
        this.timeSlot = timeSlot;
        this.isAvailable = true;
    }


}
