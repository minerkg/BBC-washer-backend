// src/main/java/com/bcc/washer/domain/Reservation.java
package com.bcc.washer.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedEntityGraph(
        name = "Reservation.withAllData",
        attributeNodes = {
                @NamedAttributeNode(value = "user"),
                @NamedAttributeNode(value = "bookableUnit", subgraph = "bookableUnit-subgraph")
        },

        subgraphs = {
                @NamedSubgraph(
                        name = "bookableUnit-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "washer"),
                                @NamedAttributeNode(value = "timeSlot", subgraph = "timeInterval-subgraph"),
                        }
                ),
                @NamedSubgraph(
                        name = "timeInterval-subgraph", attributeNodes = {
                        @NamedAttributeNode(value = "timeInterval")
                }
                )
        }
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private BookableUnit bookableUnit;

    @Enumerated(EnumType.STRING) // Store enum as String in DB
    @Column(nullable = true)
    private ReservationStatus status; // New field for reservation status
}