package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tourcategorytour")
public class TourCategoryTour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tourcategorytourid")
    private int tourCategoryTourId;

    public int getTourCategoryTourId() {
        return this.tourCategoryTourId;
    }

    public void setTourCategoryTourId(int tourCategoryTourId) {
        this.tourCategoryTourId = tourCategoryTourId;
    }
    
    @Column(name = "packageid")
    private int packageid;

    public int getPackageid() {
        return this.packageid;
    }

    public void setPackageid(int packageid) {
        this.packageid = packageid;
    }
   

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorytourid", referencedColumnName = "categorytourid")
    private CategoryTour categoryTour;

    public CategoryTour getCategoryTour() {
        return this.categoryTour;
    }

    public void setCategoryTour(CategoryTour categoryTour) {
        this.categoryTour = categoryTour;
    }

    // Getters and setters
}