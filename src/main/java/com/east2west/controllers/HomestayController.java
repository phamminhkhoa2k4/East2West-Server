package com.east2west.controllers;

import com.east2west.models.DTO.HomestayDTO;
import com.east2west.models.DTO.HomestayFilterDTO;
import com.east2west.models.DTO.HomestaySearchDTO;
import com.east2west.models.DTO.HomestaySearchRequest;
import com.east2west.models.Entity.Homestay;
import com.east2west.models.Entity.Structure;
import com.east2west.service.HomestayService;
import com.east2west.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/homestays")
public class HomestayController {

    private final HomestayService homestayService;

    @Autowired
    public HomestayController(HomestayService homestayService) {
        this.homestayService = homestayService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<HomestayDTO> getById(@PathVariable int id) {
        HomestayDTO homestay = homestayService.getById(id);
        return ResponseEntity.ok(homestay);
    }

    @GetMapping("/price/max-today")
    public BigDecimal getMaxPriceForToday() {
        return homestayService.getMaxPriceForToday();
    }
    @GetMapping("/price/min-today")
    public BigDecimal getMinPriceForToday() {
        return homestayService.getMinPriceForToday();
    }
    @GetMapping
    public ResponseEntity<List<HomestayDTO>> getAll() {
        List<HomestayDTO> homestays = homestayService.getAll();
        return ResponseEntity.ok(homestays);
    }




    @GetMapping("/structure")
    public ResponseEntity<List<Structure>> getAllStructure(){
        List<Structure> structures = homestayService.getStructureAll();
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
}
