package com.bcc.washer.domain.time;

import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.time.TimeInterval;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne( cascade = CascadeType.ALL, orphanRemoval = true)
    private TimeInterval timeInterval;

    @JsonIgnore
    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookableUnit> bookableUnit;

}
