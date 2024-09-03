package com.east2west.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.east2west.repository.*;
import com.east2west.models.DTO.CarDTO;
import com.east2west.models.Entity.Car;
import com.east2west.models.Entity.LocationType;
import com.east2west.models.Entity.Make;
import com.east2west.models.Entity.Model;
import com.east2west.models.Entity.TourPackage;
import com.east2west.models.Entity.Type;
import java.util.Optional;
import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private MakeRepository makeRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private LocationTypeRepository locationTypeRepository;

    public Car createOrUpdateCar(CarDTO carDTO) {
        Car car;
        if (carDTO.getCarId() != 0) {
            Optional<Car> optionalCar = carRepository.findById(carDTO.getCarId());
            car = optionalCar.orElse(new Car());
        } else {
            car = new Car();
        }
        car.setCarName(carDTO.getCarName());
        car.setYear(carDTO.getYear());
        car.setSeatCapacity(carDTO.getSeatCapacity());
        car.setAirConditioned(carDTO.isAirConditioned());
        car.setPricePerDay(carDTO.getPricePerDay());
        car.setStatus(carDTO.getStatus());
        car.setCargearbox(carDTO.getCargearbox());
        car.setMiles(carDTO.getMiles());
        car.setFueltankcapacity(carDTO.getFueltankcapacity());
        car.setFuel(carDTO.getFuel());
        car.setLocation(carDTO.getLocation());
        modelRepository.findById(carDTO.getModelId()).ifPresent(car::setModel);
        makeRepository.findById(carDTO.getMakeId()).ifPresent(car::setMake);
        typeRepository.findById(carDTO.getTypeId()).ifPresent(car::setType);
        locationTypeRepository.findById(carDTO.getLocationTypeId()).ifPresent(car::setLocationtype);
        return carRepository.save(car);
    }

    public boolean deleteCar(int carId) {
        if (carRepository.existsById(carId)) {
            carRepository.deleteById(carId);
            return true;
        } else {
            return false;
        }
    }

    public List<Model> getAllModel() {
        return modelRepository.findAll();
    }

    public List<Make> getAllMake() {
        return makeRepository.findAll();
    }

    public List<Type> getAllType() {
        return typeRepository.findAll();
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(int id) {
        return carRepository.findById(id).orElse(null);
    }


    public Model saveModel(Model model) {
        return modelRepository.save(model);
    }

    public Make saveMake(Make make) {
        return makeRepository.save(make);
    }

    public Type saveType(Type type) {
        return typeRepository.save(type);
    }
    public LocationType saveLocationType(LocationType locationType) {
        return locationTypeRepository.save(locationType);
    }
    public List<LocationType> getAllLocationType(){
        return locationTypeRepository.findAll();
    }
    

    // Method to delete a make by ID
    public void deleteMake(int makeId) {
        makeRepository.deleteById(makeId);
    }

    // Method to delete a type by ID
    public void deleteType(int typeId) {
        typeRepository.deleteById(typeId);
    }

    // Method to delete a model by ID
    public void deleteModel(int modelId) {
        modelRepository.deleteById(modelId);
    }

    // Method to delete a location type by ID
    public void deleteLocationType(int locationTypeId) {
        locationTypeRepository.deleteById(locationTypeId);
    }
    public boolean doesCarNameExist(String carName) {
        return carRepository.existsByCarName(carName);
    }

    public List<Car> findByName(String name) {
        return carRepository.findByTitleContainingIgnoreCase(name);
    }
}