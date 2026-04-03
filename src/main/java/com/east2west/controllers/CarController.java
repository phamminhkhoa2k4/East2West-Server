package com.east2west.controllers;


import com.east2west.models.DTO.*;
import com.east2west.models.Entity.Car;
import com.east2west.models.payload.request.CarFilterRequest;
import com.east2west.models.payload.request.SendQuoteViaEmailRequest;
import com.east2west.service.CarService;
import com.east2west.service.EmailService;
import com.east2west.service.MakeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
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
    public ResponseEntity<ModelResponse<List<CarDTO>>> searchCar(
            @RequestParam(value = "longitude") @NotNull Double longitude,
            @RequestParam(value = "latitude") @NotNull Double latitude,
            @RequestParam(value = "radius") @NotNull @Positive Double radius,
            @RequestParam(value = "pickup_date") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pickupDate,
            @RequestParam(value = "dropoff_date") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dropOffDate) {
        if (dropOffDate.isBefore(pickupDate)) {
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
                .pickUpDate(pickupDate)
                .dropOffDate(dropOffDate)
                .build();

        List<CarDTO> data = carService.searchCars(request);

        if (data.isEmpty()) {
            return ResponseEntity.ok(
                    ModelResponse.<List<CarDTO>>builder()
                            .status(200)
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
            @Valid @ModelAttribute CarFilterRequest request
    ) {
        log.debug("Filter cars request: {}", request);

        if (request.getDropOffDate().isBefore(request.getPickupDate())) {
            return ResponseEntity.badRequest().body(
                    ModelResponse.<List<CarDTO>>builder()
                            .status(400)
                            .message("Drop-off date must be after pickup date")
                            .data(Collections.emptyList())
                            .build()
            );
        }

        List<CarDTO> data = carService.filterCars(request);

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
