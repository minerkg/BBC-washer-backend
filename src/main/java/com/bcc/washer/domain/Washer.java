package com.bcc.washer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private Long id;
    private String name;
    private int capacity;
    private boolean isInOrder;

    @OneToMany(mappedBy = "washer")
    private List<BookableUnit> bookableUnits;

}
