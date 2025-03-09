package com.east2west.controllers.admin.homestay;


import com.east2west.models.DTO.HomestayDTO;
import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.DTO.PhotoDeleteDTO;
import com.east2west.models.Entity.Amenities;
import com.east2west.models.Entity.Homestay;
import com.east2west.models.Entity.Structure;
import com.east2west.service.AmenitiesService;
import com.east2west.service.HomestayService;
import com.east2west.service.StructureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homestays/host")
public class HostController {

    private final HomestayService homestayService;

    private final AmenitiesService amenitiesService;

    private final StructureService structureService;
    @Autowired
    public HostController(HomestayService homestayService, AmenitiesService amenitiesService , StructureService structureService) {
        this.homestayService = homestayService;
        this.amenitiesService = amenitiesService;
        this.structureService = structureService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Homestay> updateHomestay(@PathVariable int id , @RequestBody HomestayDTO homestayDTO) {
        Homestay homestay = homestayService.updateHomestay(id,homestayDTO);
        return ResponseEntity.ok(homestay);
    }
    @PostMapping
    public ResponseEntity<Homestay> createHomestay(@Valid @RequestBody HomestayDTO homestayDTO) {
        homestayDTO.setGeom(null);
        homestayDTO.setHomestayid(null);
        homestayDTO.setIsApproved(false);
        Homestay homestay = homestayService.createHomestay(homestayDTO);
        return ResponseEntity.ok(homestay);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHomestay(@PathVariable int id) {
        homestayService.deleteHomestay(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletePhotos")
    public ResponseEntity<?> deletePhotosHomestay(@RequestBody PhotoDeleteDTO photo) {
        try {
            homestayService.deletePhotos(photo.getUrl(), photo.getId());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<HomestayDTO>> getAllHomestaysByIdUser(@PathVariable int id){
        List<HomestayDTO> homestay = homestayService.getAllByIdUser(id);
        return ResponseEntity.ok(homestay);
    }


    @PostMapping("/baseprice")
    public ResponseEntity<?> updateBasePrice(@RequestBody HomestayDTO homestayDTO){
        try {
            homestayService.updateBasePrice(homestayDTO.getHomestayid(), homestayDTO.getPricePerNight());
            return ResponseEntity.ok("Price updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the price");
        }
    }

    @PostMapping("/weekendprice")
    public ResponseEntity<?> updateWeekendPrice(@RequestBody HomestayDTO homestayDTO){
        try {
            homestayService.updateWeekendPrice(homestayDTO.getHomestayid(), homestayDTO.getPricePerNight());
            return ResponseEntity.ok("Price updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the price");
        }
    }

    // Endpoint: Get all amenities
    @GetMapping("/amenity")
    public ResponseEntity<ModelResponse<List<Amenities>>> getAllAmenities(){
        try {
            List<Amenities> data = amenitiesService.getAmenitiesAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<List<Amenities>>builder()
                            .status(200)
                            .message("OK")
                            .data(data)
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<List<Amenities>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("/structures")
    public ResponseEntity<ModelResponse<List<Structure>>> getAllStructure(){
        try {
            List<Structure> data = structureService.getAllStructure();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<List<Structure>>builder()
                            .status(200)
                            .message("OK")
                            .data(data)
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<List<Structure>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }
    }
}
