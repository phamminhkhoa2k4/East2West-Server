package com.east2west.controllers;


import com.east2west.models.DTO.*;
import com.east2west.models.Entity.Car;
import com.east2west.models.mapper.MakeMapper;
import com.east2west.models.payload.request.SendQuoteViaEmailRequest;
import com.east2west.service.CarService;
import com.east2west.service.EmailService;
import com.east2west.service.MakeService;
import com.east2west.util.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController("carController")
@RequestMapping("/api/cars")
public class CarController {

    private final EmailService emailService;

    private final CarService carService;

    private final MakeService makeService;

    public CarController(EmailService emailService, CarService carService, MakeService makeService) {
        this.emailService = emailService;
        this.carService = carService;
        this.makeService = makeService;
    }


    @GetMapping("/search")
    public ResponseEntity<ModelResponse<List<CarDTO>>> searchCar(@RequestParam(value = "longitude") Double longitude,
                                                                 @RequestParam(value = "latitude") Double latitude,
                                                                 @RequestParam(value = "radius") Double radius,
                                                                 @RequestParam(value = "pickup_date") String pickupDate,
                                                                 @RequestParam(value = "dropoff_date") String dropOffDate) {
        if (longitude == null || latitude == null) {
            return ResponseEntity.badRequest().body(
                    ModelResponse.<List<CarDTO>>builder()
                            .status(400)
                            .message("Latitude and longitude are required")
                            .data(Collections.emptyList())
                            .build()
            );
        }

        if (radius == null || radius <= 0) {
            return ResponseEntity.badRequest().body(
                    ModelResponse.<List<CarDTO>>builder()
                            .status(400)
                            .message("Radius must be greater than 0")
                            .data(Collections.emptyList())
                            .build()
            );
        }

        LocalDate pickUp;
        LocalDate dropOff;
        try {
            pickUp = DateUtil.parse(pickupDate);
            dropOff = DateUtil.parse(dropOffDate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ModelResponse.<List<CarDTO>>builder()
                            .status(400)
                            .message("Invalid date format. Expected format: yyyy-MM-dd")
                            .data(Collections.emptyList())
                            .build()
            );
        }

        if (dropOff.isBefore(pickUp)) {
            return ResponseEntity.badRequest().body(
                    ModelResponse.<List<CarDTO>>builder()
                            .status(400)
                            .message("Drop-off date must be after pickup date")
                            .data(Collections.emptyList())
                            .build()
            );
        }

        var request = CarSearchDTO.builder()
                .longitude(longitude)
                .latitude(latitude)
                .radius(radius)
                .pickUpDate(DateUtil.parse(pickupDate))
                .dropOffDate(DateUtil.parse(dropOffDate))
                .build();

        List<CarDTO> data = carService.searchCars(request);

        if (data.isEmpty()) {
            return ResponseEntity.ok(
                    ModelResponse.<List<CarDTO>>builder()
                            .status(404)
                            .message("No cars found")
                            .data(data)
                            .build());
        }
        return ResponseEntity.ok(
                ModelResponse.<List<CarDTO>>builder()
                        .status(200)
                        .message("OK")
                        .data(data)
                        .build());


    }


    @GetMapping("/makes")
    public ResponseEntity<ModelResponse<List<MakeDTO>>> getAllCarMakes() {
        List<MakeDTO> data = makeService.getAllMake();
        return ResponseEntity.ok(
                ModelResponse.<List<MakeDTO>>builder()
                        .status(200)
                        .message("OK")
                        .data(data)
                        .build());
    }


    @GetMapping("/list")
    public ResponseEntity<?> getAllCars() {
        List<CarDTO> data = carService.getAllCars();
        return ResponseEntity.ok(
                ModelResponse.builder()
                        .status(200)
                        .message("OK")
                        .data(data)
                        .build());
    }

    @GetMapping("/filters")
    public ResponseEntity<ModelResponse<List<CarDTO>>> filterCars(
            @RequestParam(name = "gearbox", required = false) List<String> gearbox,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "type", required = false) List<String> type,
            @RequestParam(name = "information", required = false) List<String> information,
            @RequestParam(name = "engine", required = false) List<String> engine,
            @RequestParam(name = "mileage", required = false) List<String> mileage,
            @RequestParam(name = "make", required = false) List<String> make,
            @RequestParam(name = "location", required = false) List<String> location,
            @RequestParam(name = "fuelPolicy", required = false) String fuelPolicy,
            @RequestParam(name = "rating", required = false) List<String> rating,
            @RequestParam(name = "deposit", required = false) List<String> deposit,
            @RequestParam(name = "latitude", required = true) Double latitude,
            @RequestParam(name = "longitude", required = true) Double longitude,
            @RequestParam(name = "pickupDate", required = true) LocalDate pickupDate,
            @RequestParam(name = "dropOffDate", required = true) LocalDate dropOffDate,
            @RequestParam(name = "radius", required = true) Double radius
    ) {

        System.out.println("gearbox = " + gearbox);
        System.out.println("type = " + type);
        System.out.println("information = " + information);
        System.out.println("engine = " + engine);
        System.out.println("mileage = " + mileage);
        System.out.println("make = " + make);
        System.out.println("location = " + location);
        System.out.println("fuelPolicy = " + fuelPolicy);
        System.out.println("rating = " + rating);
        System.out.println("deposit = " + deposit);

        List<CarDTO> data = carService.filterCars(sort,gearbox, type, information, engine, mileage, make, location, fuelPolicy, rating, deposit, latitude, longitude, pickupDate, dropOffDate, radius);

        return ResponseEntity.ok(
                ModelResponse.<List<CarDTO>>builder()
                        .status(200)
                        .message("OK")
                        .data(data)
                        .build());
    }

    @PostMapping("/sendQuoteViaEmail")
    public ResponseEntity<?> sendQuoteViaEmail(@RequestBody SendQuoteViaEmailRequest email) {
        Optional<CarDTO> car = carService.getCarById(email.getCarId());
        if (car.isEmpty()) {
            return ResponseEntity.ok(
                    ModelResponse.builder()
                            .status(404)
                            .message("Car not found")
                            .data(null)
                            .build());
        }
        emailService.sendQuoteViaEmail(email.getEmail(), car.get(),email.getDropOffDate(),email.getPickUpDate(),email.getPickUpTime(),email.getDropOffTime());
        return ResponseEntity.ok(
                ModelResponse.builder()
                        .status(200)
                        .message(null)
                        .data(null)
                        .build());

    }

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

}
