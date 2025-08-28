package com.east2west.controllers;

import com.east2west.models.DTO.*;
import com.east2west.models.Entity.Amenities;
import com.east2west.models.Entity.Structure;
import com.east2west.service.AmenitiesService;
import com.east2west.service.HomestayService;
import com.east2west.service.StructureService;
import com.east2west.util.DateUtil;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/homestays")
public class HomestayController {

    private final HomestayService homestayService;
    private final StructureService structureService;

    private final AmenitiesService amenitiesService;

    @Autowired
    public HomestayController(HomestayService homestayService, StructureService structureService, AmenitiesService amenitiesService) {
        this.homestayService = homestayService;
        this.structureService = structureService;
        this.amenitiesService = amenitiesService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<HomestayDTO>>> getHomestayById(@PathVariable int id){
        try {
            Optional<HomestayDTO> data = homestayService.getHomestayById(id);
            if(data.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<HomestayDTO>>builder()
                                .status(404)
                                .message("Not found homestays !!!")
                                .data(null)
                                .build()
                );

            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<Optional<HomestayDTO>>builder()
                            .status(200)
                            .message("OK")
                            .data(data)
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<HomestayDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }


//    @GetMapping("/{id}")
//    public ResponseEntity<HomestayDTO> getById(@PathVariable int id) throws ParseException {
//        HomestayDTO homestay = homestayService.getById(id);
//        return ResponseEntity.ok(homestay);
//    }

    @GetMapping("/price/max-today")
    public BigDecimal getMaxPriceForToday() {
        return homestayService.getMaxPriceForToday();
    }
    @GetMapping("/price/min-today")
    public BigDecimal getMinPriceForToday() {
        return homestayService.getMinPriceForToday();
    }
    @GetMapping
    public ResponseEntity<List<HomestayDTO>> getAllApproved() {
        List<HomestayDTO> homestays = homestayService.getAllApproved();
        return ResponseEntity.ok(homestays);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<HomestayDTO>> getAll() {
        List<HomestayDTO> homestays = homestayService.getAll();
        return ResponseEntity.ok(homestays);
    }



    @GetMapping("/structure")
    public ResponseEntity<List<Structure>> getAllStructure(){
        List<Structure> structures = structureService.getAllStructure();
        return ResponseEntity.ok(structures);
    }

    @GetMapping("/homestayStructure/{id}")
    public List<HomestayDTO> getHomestaysByStructureId(@PathVariable int id) {
        return homestayService.getHomestaysByStructureId(id);
    }

    @GetMapping("/search")
    public List<HomestayDTO> searchHomestay(@RequestParam(value = "longitude") Double longitude,
                                      @RequestParam(value = "latitude") Double latitude,
                                      @RequestParam(value = "radius") Double radius,
                                      @RequestParam(value = "checkin_date") String checkinDate,
                                      @RequestParam(value = "checkout_date") String checkoutDate,
                                            @RequestParam(value = "status") String status,
                                      @RequestParam(value = "guests") Integer guests) {

            var request = HomestaySearchDTO.builder()
                .longitude(longitude)
                .latitude(latitude)
                .radius(radius)
                .checkinDate(DateUtil.parse(checkinDate))
                .checkoutDate(DateUtil.parse(checkoutDate))
                .guests(guests)
                .status(status)
                .build();

        return homestayService.searchHomestays(request);
    }


    @GetMapping("/filter")
    public ResponseEntity<List<HomestayDTO>> filterHomestays(@RequestParam(required = false) Integer minBeds,
                                          @RequestParam(required = false) Integer maxBeds,
                                          @RequestParam(required = false) Integer minMaxGuest,
                                          @RequestParam(required = false) Integer maxMaxGuest,
                                          @RequestParam(required = false) String type,
                                          @RequestParam(required = false) List<Integer> amenityIds) {
        HomestayFilterDTO filterDTO = new HomestayFilterDTO();
        filterDTO.setMinBeds(minBeds);
        filterDTO.setMaxBeds(maxBeds);
        filterDTO.setMinMaxGuest(minMaxGuest);
        filterDTO.setMaxMaxGuest(maxMaxGuest);
        filterDTO.setType(type);
        filterDTO.setAmenityIds(amenityIds);
        return ResponseEntity.ok(homestayService.filterHomestays(filterDTO));
    }

    // Endpoint: Get all amenities
    @GetMapping("/amenities")
    public ResponseEntity<List<AmenitiesDTO>> getAllAmenities(){
        List<AmenitiesDTO> amenities= amenitiesService.getAmenitiesAll();
        return ResponseEntity.ok(amenities);
    }

    // TODO: add model Response
    // Endpoint: Get multi amenities by multi ids
    @GetMapping("/amenities/multi")
    public ResponseEntity<List<AmenitiesDTO>> getAmenitiesByIds(@RequestParam List<Integer> ids) {
        List<AmenitiesDTO> amenities = amenitiesService.getByIdsAmenities(ids);
        return ResponseEntity.ok(amenities);
    }



}
