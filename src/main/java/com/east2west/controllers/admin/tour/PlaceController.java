package com.east2west.controllers.admin.tour;


import com.east2west.models.DTO.MealDTO;
import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.DTO.PlaceDTO;
import com.east2west.models.Entity.Place;
import com.east2west.service.PlaceService;
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
@RequestMapping("/api/tours/places")
public class PlaceController {
    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }



    // Endpoint: Create place
    @PostMapping
    public ResponseEntity<ModelResponse<PlaceDTO>> createPlace(@RequestBody PlaceDTO place) {
        try {
            Optional<Place> places = placeService.findByPlaceName(place.getPlacename());

            if(places.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<PlaceDTO>builder()
                                .status(400)
                                .message("Place name " + place.getPlacename() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            PlaceDTO data = placeService.createPlace(place);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<PlaceDTO>builder()
                            .status(201)
                            .message(data.getPlacename() + " place created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<PlaceDTO>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }


    // Endpoint: Delete place by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Place>> deletePlace(@PathVariable int id) {
        try {
            String data = placeService.deletePlace(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Place>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Place>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Place>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<PlaceDTO> getPlace(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return placeService.getAllPlaces(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "placeid")));
    }

    // Endpoint: Search place
    @GetMapping("/search")
    public List<PlaceDTO> searchPlace(@RequestParam String keyword) {
        return placeService.searchPlace(keyword);
    }


    // Endpoint: Get a place by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<PlaceDTO>>> getPlaceById(@PathVariable int id){
        try {
            Optional<PlaceDTO> data = placeService.getPlaceById(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<PlaceDTO>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<PlaceDTO>>builder()
                                .status(404)
                                .message("Not found transfer !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<PlaceDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }



    // Endpoint: Update place
    @PutMapping
    public ResponseEntity<ModelResponse<PlaceDTO>> updatePlace(@RequestBody PlaceDTO place) {
        try {
            PlaceDTO data = placeService.updatePlace(place);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<PlaceDTO>builder()
                                .status(200)
                                .message(data.getPlacename() +" place updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<PlaceDTO>builder()
                                .status(404)
                                .message("Place not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<PlaceDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Create place  by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = placeService.savePlaceFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<PlaceDTO>builder()
                            .status(201)
                            .message("Create successfully " + data +" place !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<PlaceDTO>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }

}
