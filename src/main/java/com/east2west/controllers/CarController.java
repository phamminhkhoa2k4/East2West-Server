package com.east2west.controllers;

import com.east2west.models.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.east2west.models.DTO.CarDTO;
import com.east2west.service.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }
    @GetMapping("/search")
    public List<Car> searchCars(
            @RequestParam(required = false) String carName,
            @RequestParam(required = false) String makeName,
            @RequestParam(required = false) String typeName,
            @RequestParam(required = false) Boolean airConditioned,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long minMiles,
            @RequestParam(required = false) Long maxMiles) {
        
        return carService.searchCars(carName, makeName, typeName, airConditioned, minPrice, maxPrice, location, minMiles, maxMiles);
    }
    @GetMapping("/{id}")
    public Car getCarById(@PathVariable int id) {
        return carService.getCarById(id);
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdateCar(@Valid @RequestBody CarDTO carDTO) {
        carService.createOrUpdateCar(carDTO);
        return ResponseEntity.ok().body("Car created successfully");
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<String> deleteCar(@PathVariable int carId) {
        try {
            carService.deleteCar(carId);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>("Cannot delete car: This car is referenced by other records. Please handle those dependencies first.", HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/exists")
    public boolean checkCarNameExists(@RequestParam String carName) {
        return carService.doesCarNameExist(carName);
    }

    // @DeleteMapping("/{carId}")
    // public ResponseEntity<Void> deleteCar(@PathVariable int carId) {
    //     carService.deleteCar(carId);
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping("/search/name")
    public List<Car> searchToursByTitle(@RequestParam("name") String name) {
        return  carService.findByName(name);
    }
}
