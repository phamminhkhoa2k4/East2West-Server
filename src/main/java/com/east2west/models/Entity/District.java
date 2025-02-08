package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "districts")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "districtid")
    private int districtid;

    @Column(name = "districtname")
    private String districtname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityprovinceid", referencedColumnName = "cityprovinceid")
    private CityProvince cityprovince;



}