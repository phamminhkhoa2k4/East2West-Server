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

        modelRepository.findById(carDTO.getModelId()).ifPresent(car::setModel);
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
        if (doesModelNameExist(model.getModelName(), model.getModelId())) {
            throw new IllegalArgumentException("Model name already exists.");
        }
        return modelRepository.save(model);
    }

    private boolean doesModelNameExist(String modelName, int excludeModelId) {
        return modelRepository.findByModelNameAndModelIdNot(modelName, excludeModelId).isPresent();
    }

    // Make-related methods
    public Make saveMake(Make make) {
        if (doesMakeNameExist(make.getMakeName(), make.getMakeId())) {
            throw new IllegalArgumentException("Make name already exists.");
        }
        return makeRepository.save(make);
    }

    private boolean doesMakeNameExist(String makeName, int excludeMakeId) {
        return makeRepository.findByMakeNameAndMakeIdNot(makeName, excludeMakeId).isPresent();
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


    public List<Car> findByName(String name) {
        return carRepository.findByTitleContainingIgnoreCase(name);
    }
}