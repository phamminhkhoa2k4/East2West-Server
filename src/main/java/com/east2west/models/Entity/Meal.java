package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mealid")
    private int mealid;



    @Column(name = "mealname")
    private String mealname;



    @Column(name = "mealthumbnail")
    private String mealthumbnail;


    @Column(name = "mealduration")
    private String mealduration;


    @Column(name = "mealactivity")
    private String mealactivity;



    
}
