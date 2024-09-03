package com.east2west.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.east2west.models.DTO.CancelDTO;
import com.east2west.models.DTO.RentalDTO;
import com.east2west.models.Entity.Rental;
import com.east2west.service.PDFService;
import com.east2west.service.RentalCarService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/rental")
public class RentalCarController {

    @Autowired
    private RentalCarService rentalCarService;

    @Autowired
    private PDFService pdfService;

    @GetMapping
    public List<Rental> getAllBookings() {
        return rentalCarService.getAllBookings();
    }

    @PostMapping
    public Rental saveRental(@RequestBody RentalDTO rentalDTO) {
        return rentalCarService.saveRental(rentalDTO);
    }

    @GetMapping("/user/{userId}")
    public List<Rental> getRentalsByUserId(@PathVariable int userId) {
        return rentalCarService.getRentalsByUserId(userId);
    }
    
    @GetMapping("/pdf/{rentalid}")
    public ResponseEntity<?> downloadRentalPDF(@PathVariable int rentalid) {
        Rental rental = rentalCarService.findById(rentalid);
        if (rental == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayInputStream bis;
        try {
            bis = pdfService.generateRentalPDF(rental);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error generating PDF: " + e.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=rental_" + rentalid + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
