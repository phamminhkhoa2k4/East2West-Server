package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "themetour")
public class ThemeTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "themetourid")
    private int themeTourId;

    @Column(name = "themetourname")
    private String themeTourName;

    // Getters and setters
    public int getThemeTourId() {
        return themeTourId;
    }

    public void setThemeTourId(int themeTourId) {
        this.themeTourId = themeTourId;
    }

    public String getThemeTourName() {
        return themeTourName;
    }

    public void setThemeTourName(String themeTourName) {
        this.themeTourName = themeTourName;
    }
}