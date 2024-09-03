package com.east2west.models.Entity;

import com.east2west.util.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Entity
@Table(name = "homestays")
public class Homestay {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestayid")
    private int homestayid;


    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wardid", referencedColumnName = "wardid")
    private Ward ward;

    // alter small
    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "structureid", referencedColumnName = "structureid")
    private Structure structure;

    @Setter
    @Getter
    @Column(name = "type")
    private String type;


    @Setter
    @Getter
    @Column(name = "userid")
    private int userid;

    @Setter
    @Getter
    @Column(name = "longitude")
    private double longitude;

    @Setter
    @Getter
    @Column(name = "latitude")
    private double latitude;

    @Setter
    @Getter
    @Column(name = "title")
    private String title;

    @Setter
    @Getter
    @Column(name = "address")
    private String address;

    @Getter
    @Setter
    @Column(name = "geom")
    private String geom;



    @Setter
    @Getter
    @Column(name = "photos")
    @Convert(converter = StringListConverter.class)
    private List<String> photos;

    @Setter
    @Getter
    @Column(name = "description")
    private String description;

    @Setter
    @Getter
    @Column(name = "extrainfo")
    private String extrainfo;

    @Setter
    @Getter
    @Column(name = "cleaningfee")
    private BigDecimal cleaningfee;

    @Setter
    @Getter
    @Column(name = "isapproved")
    private boolean isapproved;

    @Setter
    @Getter
    @Column(name = "maxguest")
    private int maxguest;

    // Getters and Setters
    @Setter
    @Getter
    @ManyToMany
    @JoinTable(
        name = "homestayperks",
        joinColumns = @JoinColumn(name = "homestayid"),
        inverseJoinColumns = @JoinColumn(name = "perkid")
    )
    private List<Perk> perks;

    @Setter
    @Getter
    @OneToMany(mappedBy = "homestay", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HomestayAvailability> homestayAvailabilityList;
}
