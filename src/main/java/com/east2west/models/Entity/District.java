package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

}