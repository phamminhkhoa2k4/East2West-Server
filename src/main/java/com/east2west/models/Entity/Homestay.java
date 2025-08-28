package com.east2west.models.Entity;

import com.east2west.models.enums.EHomestayStatus;
import com.east2west.util.StringListConverter;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Null;
import org.springframework.data.annotation.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
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
    private Boolean instant;


    @Column(name = "bathroom")
    private int bathroom;


    @Column(name = "room")
    private int room;


    @Column(name = "beds")
    private int beds;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EHomestayStatus status;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "homestayamenities",
            joinColumns = @JoinColumn(name = "homestayid"),
            inverseJoinColumns = @JoinColumn(name = "amenitiesid")
    )
    private List<Amenities> amenities;


    @OneToMany(mappedBy = "homestay", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HomestayAvailability> homestayAvailabilityList;


    @Version
    @Column(name = "version")
    private int version;

    @CreatedBy
    @Column(name = "creator", updatable = false)
    private Integer creator;

    @LastModifiedBy
    @Column(name = "modifier")
    private Integer modifier;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}