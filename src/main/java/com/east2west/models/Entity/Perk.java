package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "perks")
public class Perk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perkid")
    private int amenitiesid;


    @Column(name = "perkname")
    private String amenitiesname;



    // Getters and Setters

}
