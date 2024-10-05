package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "accommodations")
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodationid")
    private int accommodationid;



    @Column(name = "accommodationname")
    private String accommodationname;


    @Column(name = "accommodationthumbnail")
    private String accommodationthumbnail;

    @Column(name = "roomtype")
    private String roomtype;

    @Column(name = "isbreadkfast")
    private boolean isbreadkfast;



    @Column(name = "durationaccommodation")
    private String durationaccommodation;



    @Column(name = "accommodationtype")
    private String accommodationtype;



    
    

   
}