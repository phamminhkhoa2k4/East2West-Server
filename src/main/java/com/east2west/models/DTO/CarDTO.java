package com.east2west.models.DTO;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.stream.Collectors;

import com.east2west.models.Entity.Car;

import jakarta.validation.constraints.NotBlank;

public class CarDTO {
    private int carId;

    @NotBlank(message = "Car Name is required")
    private String carName;

    @NotNull(message = "Model is required")
    private int modelId;

    @NotNull(message = "Make is required")
    private int makeId;

    @NotNull(message = "Type is required")
    private int typeId;

    @Positive(message = "Year must be positive")
    private Integer year;

    @Positive(message = "Seat Capacity must be positive")
    private Integer seatCapacity;

    private Boolean airConditioned;

    public Boolean getAirConditioned() {
        return this.airConditioned;
    }

    public void setAirConditioned(Boolean airConditioned) {
        this.airConditioned = airConditioned;
    }

    @Positive(message = "Price Per Day must be positive")
    private Double pricePerDay;

    @NotNull(message = "Location Type is required")
    private int locationTypeId;

    @NotBlank(message = "Gearbox is required")
    private String cargearbox;

    @NotBlank(message = "Miles is required")
    private String miles;

    @NotBlank(message = "Fuel Tank Capacity is required")
    private String fueltankcapacity;

    @NotBlank(message = "Fuel is required")
    private String fuel;

    @NotBlank(message = "Location is required")
    private String location;
    @NotBlank(message = "status is required")
    private String status;

    private List<String> thumbnail;

    public List<String> getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(List<String> thumbnail) {
        this.thumbnail = thumbnail;
    }
    public int getCarId() {
        return this.carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return this.carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public int getModelId() {
        return this.modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getMakeId() {
        return this.makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSeatCapacity() {
        return this.seatCapacity;
    }

    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public boolean isAirConditioned() {
        return this.airConditioned;
    }

    public void setAirConditioned(boolean airConditioned) {
        this.airConditioned = airConditioned;
    }

    public double getPricePerDay() {
        return this.pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLocationTypeId() {
        return this.locationTypeId;
    }

    public void setLocationTypeId(int locationTypeId) {
        this.locationTypeId = locationTypeId;
    }

    public String getCargearbox() {
        return this.cargearbox;
    }

    public void setCargearbox(String cargearbox) {
        this.cargearbox = cargearbox;
    }

    public String getMiles() {
        return this.miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public String getFueltankcapacity() {
        return this.fueltankcapacity;
    }

    public void setFueltankcapacity(String fueltankcapacity) {
        this.fueltankcapacity = fueltankcapacity;
    }

    public String getFuel() {
        return this.fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
