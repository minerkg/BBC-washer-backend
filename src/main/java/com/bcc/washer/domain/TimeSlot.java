package com.bcc.washer.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @OneToOne
    private TimeInterval timeInterval;

    @OneToMany(mappedBy = "timeSlot")
    private List<BookableUnit> bookableUnit;

}
