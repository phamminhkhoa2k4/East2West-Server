package com.east2west.controllers.admin.tour;


import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.DTO.TourPackageDTO;
import com.east2west.models.Entity.TourPackage;
import com.east2west.service.TourPackageService;
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
@RequestMapping("/api/tours")
public class PackageController {

    private final TourPackageService tourPackageService;

    public PackageController(TourPackageService tourPackageService) {
        this.tourPackageService = tourPackageService;
    }


    // Endpoint: Create tour package
    @PostMapping
    public ResponseEntity<ModelResponse<TourPackageDTO>> createTours(@RequestBody TourPackageDTO tour) {
        try {
            Optional<TourPackage> tours = tourPackageService.findByTitle(tour.getTitle());

            if(tours.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<TourPackageDTO>builder()
                                .status(400)
                                .message("Tour Title " + tour.getTitle() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            TourPackageDTO data = tourPackageService.createTour(tour);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<TourPackageDTO>builder()
                            .status(201)
                            .message(data.getTitle() + " tour package created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<TourPackageDTO>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<TourPackageDTO> getCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return tourPackageService.getAllCars(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "packageid")));
    }


    // Endpoint: Search tour package
    @GetMapping("/search")
    public List<TourPackageDTO> searchTourPackage(@RequestParam String keyword) {
        return tourPackageService.searchTour (keyword);
    }


    // Endpoint: check if tour package exists by title
    @PostMapping("/check-title")
    public ResponseEntity<ModelResponse<Boolean>> checkTourTitleExists(@RequestBody String title) {
        try {
            Optional<TourPackage> tours = tourPackageService.findByTitle(title);
            boolean exists = tours.isPresent();
            return ResponseEntity.ok(
                    ModelResponse.<Boolean>builder()
                            .status(200)
                            .message("Check tour title existence successful.")
                            .data(exists)
                            .build()
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Boolean>builder()
                            .status(500)
                            .message("Error Internal Server !!!")
                            .data(false)
                            .build()
            );
        }
    }



    // Endpoint: Delete tour package by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<TourPackageDTO>> deleteTour(@PathVariable int id) {
        try {
            String data = tourPackageService.deleteTour(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<TourPackageDTO>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<TourPackageDTO>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<TourPackageDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Get a tour package by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<TourPackageDTO>>> getTourPackageById(@PathVariable int id){
        try {
            Optional<TourPackageDTO> data = tourPackageService.getTourById(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<TourPackageDTO>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<TourPackageDTO>>builder()
                                .status(404)
                                .message("Not found tour package !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<TourPackageDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }




    // Endpoint: Update tour package
    @PutMapping
    public ResponseEntity<ModelResponse<TourPackageDTO>> updateTour(@RequestBody TourPackageDTO tour) {
        try {
            TourPackageDTO data = tourPackageService.updateTour(tour);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<TourPackageDTO>builder()
                                .status(200)
                                .message(data.getTitle() +" tour package updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<TourPackageDTO>builder()
                                .status(404)
                                .message("Tour package not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<TourPackageDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Create tour by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<TourPackageDTO>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {
            if (file.length == 0) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<TourPackageDTO>builder()
                                .status(400)
                                .message("No file selected !!!").data(null).build());
            }
            String data = tourPackageService.saveTourFromCSV(file);

            if (data.contains("Type")) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<TourPackageDTO>builder()
                                .status(400)
                                .message(data).data(null).build());
            }

            if (data.contains("Make or Model")) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<TourPackageDTO>builder()
                                .status(400)
                                .message(data).data(null).build());
            }

            if (data.contains("Location Type")) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<TourPackageDTO>builder()
                                .status(400)
                                .message(data).data(null).build());
            }





            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<TourPackageDTO>builder()
                            .status(201)
                            .message("Create successfully " + data +" tour !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<TourPackageDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );
        }
    }

}
