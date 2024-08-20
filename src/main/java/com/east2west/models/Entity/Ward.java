package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wards")
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wardid")
    private int wardid;

    @Column(name = "wardname")
    private String wardname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "districtid", referencedColumnName = "districtid")
    private District district;

    // Getters and Setters
    public int getWardid() {
        return wardid;
    }

    public void setWardid(int wardid) {
        this.wardid = wardid;
    }

    public String getWardname() {
        return wardname;
    }

    public void setWardname(String wardname) {
        this.wardname = wardname;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}
