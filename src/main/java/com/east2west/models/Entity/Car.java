package com.east2west.models.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "Cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carid")
    private int carId;

    public int getCarId() {
        return this.carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    @Column(name = "carname")
    private String carName;

    public String getCarName() {
        return this.carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modelid", referencedColumnName = "modelid")
    private Model model;

    public Model getModel() {
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "makeid", referencedColumnName = "makeid")
    private Make make;

    public Make getMake() {
        return this.make;
    }

    public void setMake(Make make) {
        this.make = make;
    }

     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typeid", referencedColumnName = "typeid")
    private Type type;

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    @Column(name = "year")
    private int year;

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Column(name = "seatcapacity")
    private int seatCapacity;

    public int getSeatCapacity() {
        return this.seatCapacity;
    }

    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    @Column(name = "airconditioned")
    private boolean airConditioned;

    public boolean isAirConditioned() {
        return this.airConditioned;
    }

    public void setAirConditioned(boolean airConditioned) {
        this.airConditioned = airConditioned;
    }

    @Column(name = "priceperday")
    private double pricePerDay;

    public double getPricePerDay() {
        return this.pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    @Column(name = "status")
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Constructors, getters, setters, etc.
}