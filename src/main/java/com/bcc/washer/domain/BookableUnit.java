package com.bcc.washer.domain;

import com.bcc.washer.domain.reservation.Reservation;
import com.bcc.washer.domain.time.TimeSlot;
import com.bcc.washer.domain.washer.Washer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(
        name = "BookableUnit.withAllData",
        attributeNodes = {
                @NamedAttributeNode(value = "washer"),
                @NamedAttributeNode(value = "timeSlot", subgraph = "timeSlot-subgraph")
        },

        subgraphs = {
                @NamedSubgraph(
                        name = "timeSlot-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "timeInterval")
                        }
                )

        }
)
public class BookableUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Washer washer;

    @ManyToOne(cascade = CascadeType.ALL)
    private TimeSlot timeSlot;

    @OneToOne(mappedBy = "bookableUnit")
    private Reservation reservation;

    private boolean isAvailable;

    public BookableUnit(Washer washer, TimeSlot timeSlot) {
        this.washer = washer;
        this.timeSlot = timeSlot;
        this.isAvailable = true;
    }


}
