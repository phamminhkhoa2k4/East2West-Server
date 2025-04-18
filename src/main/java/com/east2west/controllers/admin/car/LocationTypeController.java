package com.east2west.controllers.admin.car;

import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.Entity.LocationType;
import com.east2west.service.LocationTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/cars/locationtype")
@PreAuthorize("hasAuthority('MODERATOR')")
public class LocationTypeController {

    private final LocationTypeService locationTypeService;

    public LocationTypeController(LocationTypeService locationTypeService) {
        this.locationTypeService = locationTypeService;
    }

    // Endpoint: Create location type
    @PostMapping
    public ResponseEntity<ModelResponse<LocationType>> createLocationType(@RequestBody LocationType locationType) {
        try {
            Optional<LocationType> locationTypes = locationTypeService.findByLocationTypeName(locationType.getLocationtypename());

            if(locationTypes.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<LocationType>builder()
                                .status(400)
                                .message("Location type name " + locationType.getLocationtypename() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            LocationType data = locationTypeService.saveLocationType(locationType);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<LocationType>builder()
                            .status(201)
                            .message(data.getLocationtypename() + " location type created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<LocationType>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }


    // Endpoint: Update location type
    @PutMapping
    public ResponseEntity<ModelResponse<LocationType>> updateLocationType(@RequestBody LocationType locationType) {
        try {
            LocationType data = locationTypeService.updateLocationType(locationType);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<LocationType>builder()
                                .status(200)
                                .message(data.getLocationtypename() +" location type updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<LocationType>builder()
                                .status(404)
                                .message("Location type not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<LocationType>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Get a location type by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<LocationType>>> getLocationTypeById(@PathVariable int id){
        try {
            Optional<LocationType> data = locationTypeService.getLocationTypeById(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<LocationType>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<LocationType>>builder()
                                .status(404)
                                .message("Not found location type !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<LocationType>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }

    // Endpoint: Delete location type by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<LocationType>> deleteLocationType(@PathVariable int id) {
        try {
            String data = locationTypeService.deleteLocationType(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<LocationType>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<LocationType>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<LocationType>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Search location type
    @GetMapping("/search")
    public ResponseEntity<List<LocationType>> searchLocationType(@RequestParam String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(locationTypeService.searchLocationType(keyword));
    }

    // Endpoint: Create location type by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = locationTypeService.saveLocationFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<LocationType>builder()
                            .status(201)
                            .message("Create successfully " + data +" location type !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<LocationType>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }

    // Endpoint: Pagination
    @GetMapping
    public ResponseEntity<Page<LocationType>> getLocationTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(locationTypeService.getAllLocationTypes(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "locationtypeid"))));
    }

    // Endpoint: List type
    @GetMapping("/list")
    public ResponseEntity<ModelResponse<List<LocationType>>> listType(){
        try{
            List<LocationType> data = locationTypeService.getAllLocationType();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<List<LocationType>>builder().status(200).message("OK").data(data).build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<List<LocationType>>builder().status(200).message("INTERNAL SERVER ERROR").data(null).build()
            );
        }
    }

}
