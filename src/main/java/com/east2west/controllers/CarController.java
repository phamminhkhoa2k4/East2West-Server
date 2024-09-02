package com.east2west.controllers;

import com.east2west.models.Entity.Make;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.east2west.models.DTO.CarDTO;
import com.east2west.models.Entity.Car;
import com.east2west.models.Entity.LocationType;
import com.east2west.models.Entity.Model;
import com.east2west.models.Entity.Type;
import com.east2west.service.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

// @CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping
    // @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<Car> getAllCars() {
        return carService.getAllCars();
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
    // {
    // "carName": "Accordcc",
    // "modelName": "Civic",
    // "makeName": "Honda",
    // "typeName": "Sedan",
    // "year": 2021,
    // "seatCapacity": 5,
    // "airConditioned": true,
    // "pricePerDay": 50.0,
    // "status": "Available"
    // }
    // @DeleteMapping("/{id}")
    // public void deletecar(@PathVariable int id) {
    // carService.;
    // }

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
    public Make addCarMake(@RequestBody Make make) {
        return carService.saveMake(make);
    }

    @GetMapping("/type")
    public List<Type> getCarType() {
        return carService.getAllType();
    }

    @PostMapping("/type")
    public Type addCarType(@RequestBody Type type) {
        return carService.saveType(type);
    }

    @GetMapping("/locationtypes")
    public List<LocationType> getLocationType() {
        return carService.getAllLocationType();
    }

    @PostMapping("/locationtypes")
    public LocationType setLocationType(@RequestBody LocationType locationtypeType) {
        return carService.saveLocationType(locationtypeType);
    }

    @GetMapping("/type/{id}")
    public Optional<Type> getTypeById(@PathVariable int id) {
        return carService.getAllType().stream().filter(t -> t.getTypeId() == id).findFirst();
    }

    @PutMapping("/type/{id}")
    public ResponseEntity<Type> updateType(@PathVariable int id, @RequestBody Type type) {
        Optional<Type> existingType = carService.getAllType().stream().filter(t -> t.getTypeId() == id).findFirst();
        if (existingType.isPresent()) {
            Type updatedType = existingType.get();
            updatedType.setTypeName(type.getTypeName()); // Assuming Type has a setTypeName method
            carService.saveType(updatedType);
            return ResponseEntity.ok(updatedType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/model/{id}")
    public Optional<Model> getModelById(@PathVariable int id) {
        return carService.getAllModel().stream().filter(m -> m.getModelId() == id).findFirst();

    }

    @PutMapping("/model/{id}")
    public ResponseEntity<Model> updateModel(@PathVariable int id, @RequestBody Model model) {
        Optional<Model> existingModel = carService.getAllModel().stream().filter(m -> m.getModelId() == id).findFirst();
        if (existingModel.isPresent()) {
            Model updatedModel = existingModel.get();
            updatedModel.setModelName(model.getModelName()); // Assuming Model has a setModelName method
            carService.saveModel(updatedModel);
            return ResponseEntity.ok(updatedModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/make/{id}")
    public ResponseEntity<Make> getMakeById(@PathVariable int id) {
        Optional<Make> make = carService.getAllMake().stream().filter(m -> m.getMakeId() == id).findFirst();
        return make.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/make/{id}")
    public ResponseEntity<Make> updateMake(@PathVariable int id, @RequestBody Make make) {
        Optional<Make> existingMake = carService.getAllMake().stream().filter(m -> m.getMakeId() == id).findFirst();
        if (existingMake.isPresent()) {
            Make updatedMake = existingMake.get();
            updatedMake.setMakeName(make.getMakeName()); // Assuming Make has a setMakeName method
            carService.saveMake(updatedMake);
            return ResponseEntity.ok(updatedMake);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // @DeleteMapping("/{carId}")
    // public ResponseEntity<Void> deleteCar(@PathVariable int carId) {
    //     carService.deleteCar(carId);
    //     return ResponseEntity.noContent().build();
    // }

    @DeleteMapping("/makes/{id}")
    public ResponseEntity<String> deleteMake(@PathVariable int id) {
        try {
            carService.deleteMake(id);
            return new ResponseEntity<>("Make deleted successfully.", HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>("Cannot delete make: This make is referenced by other records. Please handle those dependencies first.", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/types/{id}")
    public ResponseEntity<String> deleteType(@PathVariable int id) {
        try {
            carService.deleteType(id);
            return new ResponseEntity<>("Type deleted successfully.", HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>("Cannot delete type: This type is referenced by other records. Please handle those dependencies first.", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/models/{id}")
    public ResponseEntity<String> deleteModel(@PathVariable int id) {
        try {
            carService.deleteModel(id);
            return new ResponseEntity<>("Model deleted successfully.", HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>("Cannot delete model: This model is referenced by other records. Please handle those dependencies first.", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/locationtypes/{id}")
    public ResponseEntity<String> deleteLocationType(@PathVariable int id) {
        try {
            carService.deleteLocationType(id);
            return new ResponseEntity<>("Location type deleted successfully.", HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>("Cannot delete location type: This location type is referenced by other records. Please handle those dependencies first.", HttpStatus.CONFLICT);
        }
    }
}
