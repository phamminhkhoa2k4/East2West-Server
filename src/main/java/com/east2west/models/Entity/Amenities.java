package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "amenities")
public class Amenities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenitiesid")
    private int amenitiesid;


    @Column(name = "amenitiesname")
    private String amenitiesname;



    // Getters and Setters

}
