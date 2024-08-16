package com.east2west.controllers;

import com.east2west.models.Entity.Make;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.east2west.models.DTO.CarDTO;
import com.east2west.models.Entity.Car;
import com.east2west.models.Entity.Model;
import com.east2west.models.Entity.Type;
import com.east2west.service.*;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable int id) {
        return carService.getCarById(id);
    }

    @PostMapping
    public Car saveCar(@RequestBody CarDTO carDTO) {
        return carService.saveCar(carDTO);
    }
    
    // {
    //     "carName": "Accordcc",
    //     "modelName": "Civic",
    //     "makeName": "Honda",
    //     "typeName": "Sedan",
    //     "year": 2021,
    //     "seatCapacity": 5,
    //     "airConditioned": true,
    //     "pricePerDay": 50.0,
    //     "status": "Available"
    // }
    // @DeleteMapping("/{id}")
    // public void deletecar(@PathVariable int id) {
    //     carService.;
    // }
     
  @PutMapping
public ResponseEntity<Car> edit(@RequestBody CarDTO carDTO) {
    Car updatedCar = carService.updateCar(carDTO);
    return updatedCar != null ? new ResponseEntity<>(updatedCar, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
@DeleteMapping("/{carId}")
public ResponseEntity<Void> deleteCar(@PathVariable int carId) {
    boolean isDeleted = carService.deleteCar(carId);
    return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
    @GetMapping("/model")
    public List<Model> getCarModel() {
        return carService.getAllModel();
    }
    @PostMapping("/model")
    public Model addCarModel(@RequestBody Model model) {
        return carService.saveModel(model);
    }
    @GetMapping("/make")
    public List<Make> getCarMake() {
        return carService.getAllMake();
    }
    @PostMapping("/make")
    public Make addCarMake(@RequestBody Make  make) {
        return carService.saveMake(make);
    }
    @GetMapping("/type")
    public List<Type> getCarType() {
        return carService.getAllType();
    }
    @PostMapping("/type")
    public Type addCarType(@RequestBody Type  type) {
        return carService.saveType(type);
    }
}
