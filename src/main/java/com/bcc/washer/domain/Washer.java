// src/main/java/com/bcc/washer/domain/Washer.java
package com.bcc.washer.domain;

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

    @Enumerated(EnumType.STRING) // Store enum as String in DB
    @Column(nullable = false)
    private WasherStatus status; // Replaced 'isInOrder' with 'status'

    @JsonIgnore
    @OneToMany(mappedBy = "washer")
    private List<BookableUnit> bookableUnits;

}