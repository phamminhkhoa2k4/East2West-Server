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
import java.util.Optional;

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

    @GetMapping("/model")
    public List<Model> getModel() {
        return carService.getAllModel();
    }

    @PostMapping("/model")
    public Model createModel(@RequestBody Model model) {
        return carService.saveModel(model);
    }

//    @GetMapping("/make")
//    public List<Make> getMake() {
//        return carService.getAllMake();
//    }



    @GetMapping("/type")
    public List<Type> getType() {
        return carService.getAllType();
    }

    @PostMapping("/type")
    public Type createType(@RequestBody Type type) {
        return carService.saveType(type);
    }

    @GetMapping("/locationtypes")
    public List<LocationType> getLocationType() {
        return carService.getAllLocationType();
    }

    @PostMapping("/locationtypes")
    public LocationType createLocationType(@RequestBody LocationType locationtype) {
        return carService.saveLocationType(locationtype);
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
    @GetMapping("/locationtype/{id}")
    public ResponseEntity<LocationType> getLocationTypeById(@PathVariable int id) {
        Optional<LocationType> locationtype = carService.getAllLocationType().stream().filter(m -> m.getLocationtypeid() == id).findFirst();
        return locationtype.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/locationtype/{id}")
    public ResponseEntity<LocationType> updateLocationType(@PathVariable int id, @RequestBody LocationType locationType) {
        Optional<LocationType> existingMake = carService.getAllLocationType().stream().filter(m -> m.getLocationtypeid() == id).findFirst();
        if (existingMake.isPresent()) {
            LocationType updatedMake = existingMake.get();
            updatedMake.setLocationtypename(locationType.getLocationtypename());
            carService.saveLocationType(updatedMake);
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
    @GetMapping("/search/name")
    public List<Car> searchToursByTitle(@RequestParam("name") String name) {
        return  carService.findByName(name);
    }
}
