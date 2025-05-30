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
public class Washer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private String name;
    private int capacity;
    private boolean isInOrder;

    @OneToMany(mappedBy = "washer")
    private List<BookableUnit> bookableUnits;

}
