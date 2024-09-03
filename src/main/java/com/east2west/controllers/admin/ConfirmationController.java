package com.east2west.controllers.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.east2west.service.PackTourService;
import com.east2west.service.RentalCarService;
import com.east2west.models.Entity.BookingTour;
import com.east2west.models.Entity.Rental;

@RestController
@RequestMapping("/api/confirmations")
public class ConfirmationController {
    @Autowired
    private PackTourService packTourService;
    @Autowired
    private RentalCarService rentalCarService;

    @PutMapping("/rental/{rentalId}/confirm")
    public ResponseEntity<Rental> confirmRental(@PathVariable int rentalId) {
        Rental rental = rentalCarService.findById(rentalId);
        if (rental == null) {
            return ResponseEntity.notFound().build();
        }
        rental.setStatus("Confirmed");
        Rental confirmedRental = rentalCarService.saveBooking(rental);
        return ResponseEntity.ok(confirmedRental);
    }

    @PutMapping("/tour/{bookingTourId}/confirm")
    public ResponseEntity<String> confirmTourBooking(@PathVariable int bookingTourId) {
        Optional<BookingTour> bookingTourOpt = packTourService.getBookingTourById(bookingTourId);
        if (bookingTourOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BookingTour bookingTour = bookingTourOpt.get();
        bookingTour.setStatus("Confirmed");
        packTourService.saveBookingTour(bookingTour);
        return ResponseEntity.ok("Booking confirmed successfully");
    }

    @PutMapping("/tour/{bookingTourId}/refund")
    public ResponseEntity<String> confirmTourRefund(@PathVariable int bookingTourId) {
        Optional<BookingTour> bookingTourOpt = packTourService.getBookingTourById(bookingTourId);

        if (bookingTourOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BookingTour bookingTour = bookingTourOpt.get();
        bookingTour.setStatus("Refunded");
        packTourService.saveBookingTour(bookingTour);

        return ResponseEntity.ok("Tour refund confirmed successfully");
    }
}
