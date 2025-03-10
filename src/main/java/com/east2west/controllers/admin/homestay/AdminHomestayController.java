package com.east2west.controllers.admin.homestay;


import com.east2west.service.HomestayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/homestays/admin")
public class AdminHomestayController {
    private final HomestayService homestayService;

    public AdminHomestayController(HomestayService homestayService) {
        this.homestayService = homestayService;
    }

    @PutMapping("/approved/{id}")
    public ResponseEntity<?> approvedHomestay(@PathVariable int id){
        homestayService.approveHomestay(id);
        return ResponseEntity.ok("OK");
    }
}
