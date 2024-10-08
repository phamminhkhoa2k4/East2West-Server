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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Cars")
public class Car {

    @Id
    @Setter
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carid")
    private int carId;



    @Setter
    @Getter
    @Column(name = "carname")
    private String carName;


    @Setter
    @Getter
     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modelid", referencedColumnName = "modelid")
    private Model model;



    @Setter
    @Getter
     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "makeid", referencedColumnName = "makeid")
    private Make make;



    @Setter
    @Getter
     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typeid", referencedColumnName = "typeid")
    private Type type;




    @Setter
    @Getter
    @Column(name = "year")
    private int year;



    @Setter
    @Getter
    @Column(name = "seatcapacity")
    private int seatCapacity;


    @Setter
    @Getter
    @Column(name = "airconditioned")
    private boolean airConditioned;



    @Setter
    @Getter
    @Column(name = "priceperday")
    private double pricePerDay;



    @Setter
    @Getter
    @Column(name = "status")
    private String status;

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "locationtypeid", referencedColumnName = "locationtypeid")
    private LocationType locationtype;

    @Setter
    @Getter
    @Column(name="cargearbox")
    private String cargearbox;

    @Setter
    @Getter
    @Column(name="miles")
    private String miles;

    @Setter
    @Getter
    @Column(name="fueltankcapcity")
    private String fueltankcapacity;



    @Setter
    @Getter
    @Column(name="fuel")
    private String fuel;


    @Setter
    @Getter
    @Column(name="location")
    private String location;


@Convert(converter = StringListConverter.class)
    @Column(name = "thumbnail")
    private List<String> thumbnail;

    public List<String> getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(List<String> thumbnail) {
        this.thumbnail = thumbnail;
    }
}