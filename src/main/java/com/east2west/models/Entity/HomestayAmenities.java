package com.east2west.models.Entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "homestayamenities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HomestayAmenities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestayamenitiesid")
    private Integer homestayAmenitiesID;

    public Integer getHomestayAmenitiesID() {
        return this.homestayAmenitiesID;
    }

    public void setHomestayAmenitiesID(Integer homestayAmenitiesID) {
        this.homestayAmenitiesID = homestayAmenitiesID;
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
    @JoinColumn(name = "amenitiesid", referencedColumnName = "amenitiesid")
    private Amenities amenities;
}

