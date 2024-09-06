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

    public int getAmenitiesid() {
        return this.amenitiesid;
    }

    public void setAmenitiesid(int amenitiesid) {
        this.amenitiesid = amenitiesid;
    }

    @Column(name = "amenitiesname")
    private String amenitiesname;

    public String getAmenitiesname() {
        return this.amenitiesname;
    }

    public void setAmenitiesname(String amenitiesname) {
        this.amenitiesname = amenitiesname;
    }



    // Getters and Setters

}
