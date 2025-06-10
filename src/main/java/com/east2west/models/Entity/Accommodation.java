package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Column(name = "isbreakfast")
    private boolean isbreakfast;

    @Column(name = "durationaccommodation")
    private String durationaccommodation;

    @Column(name = "accommodationtype")
    private String accommodationtype;
   
}