package com.east2west.models.Entity;

import com.east2west.util.StringListConverter;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "homestays")
public class Homestay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestayid")
    private int homestayid;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wardid", referencedColumnName = "wardid")
    private Ward ward;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "structureid", referencedColumnName = "structureid")
    private Structure structure;


    @Column(name = "type")
    private String type;


    @Column(name = "userid")
    private int userid;


    @Column(name = "longitude")
    private double longitude;


    @Column(name = "latitude")
    private double latitude;


    @Column(name = "title")
    private String title;


    @Column(name = "address", columnDefinition = "Text")
    private String address;


    @Column(columnDefinition = "geometry(Point, 3857)")
    private Point geom;


    @Column(name = "photos", columnDefinition = "TEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> photos;


    @Column(name = "description", columnDefinition = "TEXT")
    private String description;


    @Column(name = "extrainfo", columnDefinition = "TEXT")
    private String extrainfo;


    @Column(name = "cleaningfee")
    private BigDecimal cleaningfee;


    @Column(name = "isapproved")
    private boolean isapproved;


    @Column(name = "maxguest")
    private int maxguest;


    @Column(name = "instant")
    private boolean instant;


    @Column(name = "bathroom")
    private int bathroom;


    @Column(name = "room")
    private int room;


    @Column(name = "beds")
    private int beds;


    @ManyToMany
    @JoinTable(
            name = "homestayamenities",
            joinColumns = @JoinColumn(name = "homestayid"),
            inverseJoinColumns = @JoinColumn(name = "amenitiesid")
    )
    private List<Amenities> amenities;


    @OneToMany(mappedBy = "homestay", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HomestayAvailability> homestayAvailabilityList;
}