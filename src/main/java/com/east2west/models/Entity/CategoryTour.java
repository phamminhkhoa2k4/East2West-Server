package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categorytour")
public class CategoryTour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categorytourid")
    private int categoryTourId;

    @Column(name = "categorytourname")
    private String categoryTourName;

    public int getCategoryTourId() {
        return categoryTourId;
    }

    public void setCategoryTourId(int categoryTourId) {
        this.categoryTourId = categoryTourId;
    }

    public String getCategoryTourName() {
        return categoryTourName;
    }

    public void setCategoryTourName(String categoryTourName) {
        this.categoryTourName = categoryTourName;
    }
}