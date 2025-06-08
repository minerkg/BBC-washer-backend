// src/main/java/com/bcc/washer/domain/Washer.java
package com.bcc.washer.domain.washer;

import com.bcc.washer.domain.BookableUnit;
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
public class Washer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private WasherStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "washer")
    private List<BookableUnit> bookableUnits;

}