package com.east2west.controllers;

import com.east2west.models.DTO.BookingTourDTO;
import com.east2west.service.PackTourService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingTourController {

    @Autowired
    private PackTourService packTourService;
    
    @PostMapping()
    public ResponseEntity<String> createBookingTour(@RequestBody BookingTourDTO bookingTourDTO) {
        packTourService.createBookingTour(bookingTourDTO);
        return ResponseEntity.ok("Booking tour created successfully!");
    }
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelBooking(@RequestParam int bookingTourId) {
        String response = packTourService.cancelBooking(bookingTourId);
        return ResponseEntity.ok(response);
    }
    //http://localhost:8080/api/bookings/cancel?bookingTourId=1
}