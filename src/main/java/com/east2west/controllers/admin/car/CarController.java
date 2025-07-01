package com.east2west.controllers.admin.car;


import com.east2west.models.DTO.CarDTO;
import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.Entity.Car;
import com.east2west.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }


    // Endpoint: Create car 
    @PostMapping
    public ResponseEntity<ModelResponse<CarDTO>> createCar(@RequestBody CarDTO car) {
        try {
            Optional<Car> cars = carService.findByCarName(car.getCarName());

            if(cars.isPresent()){
                    return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<CarDTO>builder()
                                .status(400)
                                .message("Car name " + car.getCarName() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            CarDTO data = carService.createCar(car);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<CarDTO>builder()
                            .status(201)
                            .message(data.getCarName() + " car created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<CarDTO>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }

    // Endpoint: Update car
    @PutMapping
    public ResponseEntity<ModelResponse<CarDTO>> updateCar(@RequestBody CarDTO car) {
        try {
            CarDTO data = carService.updateCar(car);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<CarDTO>builder()
                                .status(200)
                                .message(data.getCarName() +" car updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<CarDTO>builder()
                                .status(404)
                                .message("Car not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<CarDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Get a car by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<CarDTO>>> getCarById(@PathVariable int id){
        try {
            Optional<CarDTO> data = carService.getCarById(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<CarDTO>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<CarDTO>>builder()
                                .status(404)
                                .message("Not found car !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<CarDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }

    // Endpoint: Pagination
    @GetMapping
    public Page<CarDTO> getCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return carService.getAllCars(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "carid")));
    }

    // Endpoint: Search car
    @GetMapping("/search")
    public List<CarDTO> searchCar(@RequestParam String keyword) {
        return carService.searchCar(keyword);
    }


    // Endpoint: Delete car by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<CarDTO>> deleteCar(@PathVariable int id) {
        try {
            String data = carService.deleteCar(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<CarDTO>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<CarDTO>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<CarDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Create car by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<CarDTO>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {
            if (file.length == 0) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<CarDTO>builder()
                                .status(400)
                                .message("No file selected !!!").data(null).build());
            }
            String data = carService.saveCarFromCSV(file);

            if (data.contains("Type")) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<CarDTO>builder()
                                .status(400)
                                .message(data).data(null).build());
            }

            if (data.contains("Make or Model")) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<CarDTO>builder()
                                .status(400)
                                .message(data).data(null).build());
            }

            if (data.contains("Location Type")) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<CarDTO>builder()
                                .status(400)
                                .message(data).data(null).build());
            }





            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<CarDTO>builder()
                            .status(201)
                            .message("Create successfully " + data +" car !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<CarDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );
        }
    }
}
