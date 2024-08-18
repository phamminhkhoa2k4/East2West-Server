package com.east2west.models.Entity;

import jakarta.persistence.*;
import java.util.List;
@Entity
@Table(name = "perks")
public class Perk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perkid")
    private int perkid;

    @Column(name = "perkname")
    private String perkname;

    @ManyToMany(mappedBy = "perks")
    private List<Homestay> homestays;

    // Getters and Setters
    public int getPerkid() {
        return perkid;
    }

    public void setPerkid(int perkid) {
        this.perkid = perkid;
    }

    public String getPerkname() {
        return perkname;
    }

    public void setPerkname(String perkname) {
        this.perkname = perkname;
    }

    public List<Homestay> getHomestays() {
        return homestays;
    }

    public void setHomestays(List<Homestay> homestays) {
        this.homestays = homestays;
    }
}
