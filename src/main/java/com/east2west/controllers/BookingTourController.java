package com.east2west.controllers;

import com.east2west.models.DTO.BookingTourDTO;
import com.east2west.models.DTO.CancelDTO;
import com.east2west.models.Entity.BookingTour;
import com.east2west.service.PackTourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/bookings")
public class BookingTourController {
    @Autowired
    private PackTourService packTourService;
    @GetMapping
    public List<BookingTour> getBookingsTourList() {
        return packTourService.getBookingTour();
    }

    @PostMapping()
    public ResponseEntity<String> createBookingTour(@RequestBody BookingTourDTO bookingTourDTO) {
        packTourService.createBookingTour(bookingTourDTO);
        return ResponseEntity.ok("Booking tour created successfully!");
    }
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelBooking(@RequestBody CancelDTO cancelDTO) {
        String response = packTourService.cancelBooking(cancelDTO);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/user/{userId}")
    public List<BookingTour> getRentalsByUserId(@PathVariable int userId) {
        return packTourService.getListBookingByUser(userId);
    }
}