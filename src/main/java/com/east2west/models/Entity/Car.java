package com.east2west.models.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import com.east2west.util.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.*;
import lombok.Setter;

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
    private int carId;


    @Column(name = "carname")
    private String carName;


     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modelid", referencedColumnName = "modelid")
    private Model model;



     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "makeid", referencedColumnName = "makeid")
    private Make make;


     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typeid", referencedColumnName = "typeid")
    private Type type;



    @Column(name = "year")
    private int year;


    @Column(name = "seatcapacity")
    private int seatCapacity;


    @Column(name = "airconditioned")
    private boolean airConditioned;


    @Column(name = "priceperday")
    private double pricePerDay;


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

    @Column(name="location")
    private String location;


    @Convert(converter = StringListConverter.class)
    @Column(name = "thumbnail", columnDefinition = "Text")
    private List<String> thumbnail;

}