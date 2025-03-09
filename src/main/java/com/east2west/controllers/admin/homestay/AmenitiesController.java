package com.east2west.controllers.admin.homestay;


import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.Entity.Amenities;
import com.east2west.service.AmenitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/homestays/host/amenities")
@PreAuthorize("hasAuthority('MODERATOR')")
public class AmenitiesController {
    private final AmenitiesService amenitiesService;

    @Autowired
    public AmenitiesController( AmenitiesService amenitiesService) {
        this.amenitiesService = amenitiesService;
    }

    // Endpoint: Create amenities
    @PostMapping
    public ResponseEntity<ModelResponse<Amenities>> createAmenities(@RequestBody Amenities amenities) {
        try {
            Amenities data = amenitiesService.createAmenities(amenities);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<Amenities>builder()
                            .status(201)
                            .message(data.getAmenitiesname() + " amenities created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Amenities>builder()
                            .status(500)
                            .message("Failed to create amenities !!!")
                            .data(null)
                            .build()
            );

        }

    }

    // Endpoint: Update amenities
    @PutMapping
    public ResponseEntity<ModelResponse<Amenities>> updateAmenities(@RequestBody Amenities amenities) {
        try {
            Amenities data = amenitiesService.updateAmenities(amenities);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Amenities>builder()
                                .status(200)
                                .message(data.getAmenitiesname() +" amenities updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Amenities>builder()
                                .status(404)
                                .message("Amenities not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Amenities>builder()
                            .status(500)
                            .message("Failed to updated amenities !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Create amenities by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = amenitiesService.saveAmenitiesFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<Amenities>builder()
                            .status(201)
                            .message("Create successfully " + data +" amenities !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Amenities>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }

    // Endpoint: Get an amenities by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<Amenities>>> getAmenitiesById(@PathVariable int id){
        try {
            Optional<Amenities> data = amenitiesService.getByIdAmenities(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<Amenities>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<Amenities>>builder()
                                .status(404)
                                .message("Not found amenities !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<Amenities>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }


    // Endpoint: Get all amenities
    @GetMapping("/amenities")
    public ResponseEntity<List<Amenities>> getAllAmenities(){
        List<Amenities> amenities= amenitiesService.getAmenitiesAll();
        return ResponseEntity.ok(amenities);
    }

    // Endpoint: Search amenities
    @GetMapping("/search")
    public List<Amenities> searchAmenities(@RequestParam String keyword) {
        return amenitiesService.searchAmenities(keyword);
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<Amenities> getAmenities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return amenitiesService.getAllAmenities(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"amenitiesid")));
    }

    // Endpoint: Delete amenities by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Amenities>> deleteAmenities(@PathVariable int id) {
        try {
            String data = amenitiesService.deleteAmenities(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Amenities>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Amenities>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Amenities>builder()
                            .status(500)
                            .message("Failed to deleted amenities !!!")
                            .data(null)
                            .build()
            );

        }
    }
}
