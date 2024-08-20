package com.east2west.models.Entity;

import jakarta.persistence.*;


@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mealid")
    private int mealid;

    public int getMealid() {
        return this.mealid;
    }

    public void setMealid(int mealid) {
        this.mealid = mealid;
    }

    @Column(name = "mealname")
    private String mealname;

    public String getMealname() {
        return this.mealname;
    }

    public void setMealname(String mealname) {
        this.mealname = mealname;
    }

    @Column(name = "mealthumbnail")
    private String mealthumbnail;

    public String getMealthumbnail() {
        return this.mealthumbnail;
    }

    public void setMealthumbnail(String mealthumbnail) {
        this.mealthumbnail = mealthumbnail;
    }

    @Column(name = "mealduration")
    private String mealduration;

    public String getMealduration() {
        return this.mealduration;
    }

    public void setMealduration(String mealduration) {
        this.mealduration = mealduration;
    }

    @Column(name = "mealactivity")
    private String mealactivity;

    public String getMealactivity() {
        return this.mealactivity;
    }

    public void setMealactivity(String mealactivity) {
        this.mealactivity = mealactivity;
    }

    
}
