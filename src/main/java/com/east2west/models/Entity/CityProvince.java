package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "citiesprovinces")
public class CityProvince {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cityprovinceid")
    private int cityprovinceid;

    @Column(name = "cityname")
    private String cityname;

    // Getters and Setters
    public int getCityprovinceid() {
        return cityprovinceid;
    }

    public void setCityprovinceid(int cityprovinceid) {
        this.cityprovinceid = cityprovinceid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }
}