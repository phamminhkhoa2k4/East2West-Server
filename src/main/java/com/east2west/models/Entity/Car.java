package com.east2west.models.Entity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import com.east2west.util.StringListConverter;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carid")
    private int carid;


    @Column(name = "carname")
    private String carName;


     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modelid", referencedColumnName = "modelid")
    private Model model;


    @Column(name = "longitude")
    private double longitude;


    @Column(name = "latitude")
    private double latitude;

    @Column(name="location",columnDefinition = "Text")
    private String location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wardid", referencedColumnName = "wardid")
    private Ward ward;

    @Column(columnDefinition = "geometry(Point, 3857)")
    private Point geom;


     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typeid", referencedColumnName = "typeid")
    private Type type;



    @Column(name = "year")
    private int year;

    @Column(name = "largeLuggage")
    private int largeLuggage;

    @Column(name = "smallLuggage")
    private int smallLuggage;


    @Column(name = "seatcapacity")
    private int seatCapacity;


    @Column(name = "airconditioned")
    private boolean airConditioned;

    @Column(name = "fourdoorsormore")
    private boolean fourDoorsOrMore;


    @Column(name = "quantity")
    private Integer quantity;


    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "locationtypeid", referencedColumnName = "locationtypeid")
    private LocationType locationtype;


    @Column(name="cargearbox")
    private String cargearbox;


    @Column(name="miles")
    private String miles;

    @Column(name="fueltankcapacity")
    private String fueltankcapacity;


    @Column(name="fuel")
    private String fuel;

    @Column(name="fuelsamereturn")
    private Boolean  fuelSameReturn;

    @Column(name="enginesystem")
    private String enginesystem;


    @Convert(converter = StringListConverter.class)
    @Column(name = "thumbnail", columnDefinition = "Text")
    private List<String> thumbnail;

    @DecimalMin("0.00")
    @DecimalMax("99.99")
    @Column(precision = 4, scale = 2)
    private BigDecimal deposit;


    @Column(name="cancelfree")
    private Boolean cancelFree;


    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CarAvailability> carAvailabilityList;


}