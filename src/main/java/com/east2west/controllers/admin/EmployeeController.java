package com.east2west.controllers.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.east2west.service.PackTourService;
import com.east2west.service.RentalCarService;
import com.east2west.models.DTO.BookingTourDTO;
import com.east2west.models.DTO.CancelDTO;
import com.east2west.models.DTO.RentalDTO;
import com.east2west.models.Entity.BookingTour;
import com.east2west.models.Entity.Rental;

@RestController
@RequestMapping("/api/employee-bookings")
public class EmployeeController {

    @Autowired
    private RentalCarService rentalCarService;
    @Autowired
    private PackTourService packTourService;

    @PostMapping("/rental")
    public ResponseEntity<Rental> bookRentalDirectly(@RequestBody RentalDTO rentalDTO) {
        Rental rental = rentalCarService.saveRental(rentalDTO);
        rental.setStatus("Confirmed");
        Rental confirmedRental = rentalCarService.saveBooking(rental);
        return ResponseEntity.ok(confirmedRental);
    }

    @PostMapping("/tour")
    public ResponseEntity<String> bookTourDirectly(@RequestBody BookingTourDTO bookingTourDTO) {
        packTourService.eployeeSaveBookingTour(bookingTourDTO);
        return ResponseEntity.ok("Booking tour created successfully!");
    }

    @PostMapping("/refund")
    public ResponseEntity<String> processEmployeeRefund(@RequestBody CancelDTO refundRequest) {
        packTourService.cancelBookingEmployee(refundRequest);
        return ResponseEntity.badRequest().body("Invalid refund request");
    }
}