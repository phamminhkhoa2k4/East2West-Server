package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "suitabletour")
public class SuitableTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suitabletourid")
    private int suitableTourId;

    @Column(name = "suitablename")
    private String suitableName;

    // Getters and setters

    public int getSuitableTourId() {
        return suitableTourId;
    }

    public void setSuitableTourId(int suitableTourId) {
        this.suitableTourId = suitableTourId;
    }

    public String getSuitableName() {
        return suitableName;
    }

    public void setSuitableName(String suitableName) {
        this.suitableName = suitableName;
    }

}