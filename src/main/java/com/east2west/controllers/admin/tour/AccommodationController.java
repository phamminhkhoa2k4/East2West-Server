package com.east2west.controllers.admin.tour;


import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.DTO.AccommodationDTO;
import com.east2west.models.Entity.Accommodation;
import com.east2west.service.AccommodationService;
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
@RequestMapping("/api/tours/accommodation")
public class AccommodationController {


    private final AccommodationService accommodationService;

    @Autowired
    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    // Endpoint: Create Accommodation
    @PostMapping
    public ResponseEntity<ModelResponse<AccommodationDTO>> createAccommodation(@RequestBody AccommodationDTO accommodationDto) {
        try {
            Optional<Accommodation> accommodation = accommodationService.findByAccommodationName(accommodationDto.getAccommodationname());

            if(accommodation.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<AccommodationDTO>builder()
                                .status(400)
                                .message("Accommodation name " + accommodationDto.getAccommodationname() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            AccommodationDTO data = accommodationService.createAccommodation(accommodationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<AccommodationDTO>builder()
                            .status(201)
                            .message(data.getAccommodationname() + " accommodation created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<AccommodationDTO>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }


    // Endpoint: Delete accommodation by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Accommodation>> deleteAccommodation(@PathVariable int id) {
        try {
            String data = accommodationService.deleteAccommodation(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Accommodation>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Accommodation>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Accommodation>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<AccommodationDTO> getAccommodations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return accommodationService.getAllAccommodations(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "accommodationid")));
    }

    // Endpoint: Search accommodation
    @GetMapping("/search")
    public List<AccommodationDTO> searchAccommodation(@RequestParam String keyword) {
        return accommodationService.searchAccommodation(keyword);
    }


    // Endpoint: Get a accommodation by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<AccommodationDTO>>> getAccommodationById(@PathVariable int id){
        try {
            Optional<AccommodationDTO> data = accommodationService.getAccommodationById(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<AccommodationDTO>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<AccommodationDTO>>builder()
                                .status(404)
                                .message("Not found accommodation !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<AccommodationDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }



    // Endpoint: Update accommodation
    @PutMapping
    public ResponseEntity<ModelResponse<AccommodationDTO>> updateAccommodation(@RequestBody AccommodationDTO accommodationDto) {
        try {
            AccommodationDTO data = accommodationService.updateAccommodation(accommodationDto);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<AccommodationDTO>builder()
                                .status(200)
                                .message(data.getAccommodationname() +" accommodation updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<AccommodationDTO>builder()
                                .status(404)
                                .message("Accommodation not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<AccommodationDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Create accommodation  by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = accommodationService.saveAccommodationFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<AccommodationDTO>builder()
                            .status(201)
                            .message("Create successfully " + data +" accommodation !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<AccommodationDTO>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }
}
