package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "districts")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "districtid")
    private int districtid;

    @Column(name = "districtname")
    private String districtname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityid", referencedColumnName = "cityprovinceid")
    private CityProvince cityprovince;

    // Getters and Setters
    public int getDistrictid() {
        return districtid;
    }

    public void setDistrictid(int districtid) {
        this.districtid = districtid;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    public CityProvince getCityprovince() {
        return cityprovince;
    }

    public void setCityprovince(CityProvince cityprovince) {
        this.cityprovince = cityprovince;
    }
}