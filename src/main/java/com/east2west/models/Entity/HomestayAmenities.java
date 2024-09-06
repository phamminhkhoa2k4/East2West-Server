package com.east2west.models.Entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
<<<<<<< HEAD:src/main/java/com/east2west/models/Entity/HomestayPerk.java
@Table(name = "homestayperks")
public class HomestayPerk {
=======
@Table(name = "homestayamenities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HomestayAmenities {
>>>>>>> ad3c44083ad3a7ae2509377f86d26ba516a90ea7:src/main/java/com/east2west/models/Entity/HomestayAmenities.java

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestayamenitiesid")
    private Integer homestayAmenitiesID;

    public Integer getHomestayPerkID() {
        return this.homestayPerkID;
    }

    public void setHomestayPerkID(Integer homestayPerkID) {
        this.homestayPerkID = homestayPerkID;
    }

    @ManyToOne
    @JoinColumn(name = "homestayid", referencedColumnName = "homestayid")
    private Homestay homestay;

    public Homestay getHomestay() {
        return this.homestay;
    }

    public void setHomestay(Homestay homestay) {
        this.homestay = homestay;
    }

    @ManyToOne
<<<<<<< HEAD:src/main/java/com/east2west/models/Entity/HomestayPerk.java
    @JoinColumn(name = "perkid", referencedColumnName = "perkid")
    private Perk perk;

    public Perk getPerk() {
        return this.perk;
    }

    public void setPerk(Perk perk) {
        this.perk = perk;
    }

=======
    @JoinColumn(name = "amenitiesid", referencedColumnName = "amenitiesid")
    private Amenities amenities;
>>>>>>> ad3c44083ad3a7ae2509377f86d26ba516a90ea7:src/main/java/com/east2west/models/Entity/HomestayAmenities.java
}

