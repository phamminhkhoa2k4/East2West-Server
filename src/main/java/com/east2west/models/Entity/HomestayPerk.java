package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "homestayperks")
public class HomestayPerk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestayperkid")
    private Integer homestayPerkID;

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
    @JoinColumn(name = "perkid", referencedColumnName = "perkid")
    private Perk perk;

    public Perk getPerk() {
        return this.perk;
    }

    public void setPerk(Perk perk) {
        this.perk = perk;
    }

}

