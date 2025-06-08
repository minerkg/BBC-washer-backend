package com.bcc.washer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;


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

    @OneToOne( cascade = CascadeType.ALL, orphanRemoval = true)
    private TimeInterval timeInterval;

    @JsonIgnore
    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookableUnit> bookableUnit;


    public boolean equals(TimeSlot that) {
        if (this == that) return true;
        if (that == null) return false;
        return this.getTimeInterval().equals(that.getTimeInterval());
    }


}
