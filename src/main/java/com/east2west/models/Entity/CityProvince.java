package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "citiesprovinces")
public class CityProvince {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cityprovinceid")
    private int cityprovinceid;

    @Column(name = "cityname")
    private String cityname;

    // Getters and Setters

}