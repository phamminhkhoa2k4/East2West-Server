package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
<<<<<<< HEAD:src/main/java/com/east2west/models/Entity/Perk.java
@Table(name = "perks")
public class Perk {
=======
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "amenities")
public class Amenities {
>>>>>>> ad3c44083ad3a7ae2509377f86d26ba516a90ea7:src/main/java/com/east2west/models/Entity/Amenities.java

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
