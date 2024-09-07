package com.east2west.controllers;

import com.east2west.models.DTO.HomestayDTO;
import com.east2west.models.Entity.Homestay;
import com.east2west.service.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<List<HomestayDTO>> getAll() {
        List<HomestayDTO> homestays = homestayService.getAll();
        return ResponseEntity.ok(homestays);
    }

//    @GetMapping
//    public List<Homestay> searchHomestay(@RequestParam("checkin_date") String checkInDate,
//                                               @RequestParam("checkout_date") String checkOutDate,
//                                               @RequestParam("guests") Integer guests,
//                                               @RequestParam(value = "ward_id", required = false) Integer wardId,
//                                               @RequestParam(value = "district_id", required = false) Integer districtId,
//                                               @RequestParam(value = "city_id", required = false) Integer cityId,
//                                               @RequestParam(value = "state_id", required = false) Integer stateId) {
//        var request = HomestaySearchRequest.builder()
//                .checkInDate(checkInDate)
//                .checkOutDate(checkOutDate)
//                .guests(guests)
//                .wardId(wardId)
//                .districtId(districtId)
//                .cityId(cityId)
//                .stateId(stateId)
//                .build();
//
//        return service.searchHomestays(request);
//    }
}
