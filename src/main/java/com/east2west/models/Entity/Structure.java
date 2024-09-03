package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "structures")
public class Structure {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "structureid")
    private int structureid;


    @Getter
    @Setter
    @Column(name = "structurename")
    private String structurename;

    // Getters and Setters

}
