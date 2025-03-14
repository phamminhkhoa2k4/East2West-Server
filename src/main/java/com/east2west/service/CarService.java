package com.east2west.service;

import com.east2west.models.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.east2west.repository.*;
import com.east2west.models.DTO.CarDTO;
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
        boolean isNewCar = carDTO.getCarId() == 0;

        if (!isNewCar) {
            Optional<Car> optionalCar = carRepository.findById(carDTO.getCarId());
            car = optionalCar.orElse(new Car());
        } else {
            car = new Car();
        }

        // Check for duplicate car name
        if (doesCarNameExist(carDTO.getCarName(), carDTO.getCarId())) {
            throw new IllegalArgumentException("Car name already exists.");
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
        car.setThumbnail(carDTO.getThumbnail());
//        modelRepository.findById(carDTO.getModelId()).ifPresent(car::setModel);
        makeRepository.findById(carDTO.getMakeId()).ifPresent(car::setMake);
        typeRepository.findById(carDTO.getTypeId()).ifPresent(car::setType);
        locationTypeRepository.findById(carDTO.getLocationTypeId()).ifPresent(car::setLocationtype);

        return carRepository.save(car);
    }
    private boolean doesCarNameExist(String carName, int excludeCarId) {
        // Check if the car name exists, excluding the current car
        return carRepository.findByCarNameAndCarIdNot(carName, excludeCarId).isPresent();
    }

    // Other methods...

    public boolean doesCarNameExist(String carName) {
        return carRepository.existsByCarName(carName);
    }

    // New repository method
    public Optional<Car> findByCarNameAndIdNot(String carName, int excludeCarId) {
        return carRepository.findByCarNameAndCarIdNot(carName, excludeCarId);
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



    public List<Type> getAllType() {
        return typeRepository.findAll();
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(int id) {
        return carRepository.findById(id).orElse(null);
    }



    // Type-related methods
    public Type saveType(Type type) {
        if (doesTypeNameExist(type.getTypeName(), type.getTypeId())) {
            throw new IllegalArgumentException("Type name already exists.");
        }
        return typeRepository.save(type);
    }

    private boolean doesTypeNameExist(String typeName,int excludeTypeId) {
        return typeRepository.findByTypeNameAndTypeIdNot(typeName, excludeTypeId).isPresent();
    }

    // LocationType-related methods
    public LocationType saveLocationType(LocationType locationType) {
        if (doesLocationTypeNameExist(locationType.getLocationtypename(), locationType.getLocationtypeid())) {
            throw new IllegalArgumentException("Location type name already exists.");
        }
        return locationTypeRepository.save(locationType);
    }

    private boolean doesLocationTypeNameExist(String locationTypeName, int excludeLocationTypeId) {
        return locationTypeRepository.findByLocationtypenameAndLocationtypeidNot(locationTypeName, excludeLocationTypeId).isPresent();
    }
    public List<LocationType> getAllLocationType(){
        return locationTypeRepository.findAll();
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


    public List<Car> findByName(String name) {
        return carRepository.findByTitleContainingIgnoreCase(name);
    }
    public List<Car> searchCars(String carName, String makeName, String typeName,
                                   Boolean airConditioned, Double minPrice, Double maxPrice,
                                   String location, Long minMiles, Long maxMiles) {
        
        return carRepository.findByFilters(carName, makeName, typeName, airConditioned, minPrice, maxPrice, location, minMiles, maxMiles);
    }
}