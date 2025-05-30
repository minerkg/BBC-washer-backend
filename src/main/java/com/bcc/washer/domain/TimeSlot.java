package com.bcc.washer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    private Long id;

    @OneToOne
    private TimeInterval timeInterval;

    @OneToMany(mappedBy = "timeSlot")
    private List<BookableUnit> bookableUnit;

}
