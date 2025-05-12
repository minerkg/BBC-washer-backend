package com.bcc.washer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Washer {

    @Id
    private Long id;
    private String name;
    private int capacity;
    private boolean isInOrder;


}
