package com.east2west.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.east2west.models.DTO.RentalDTO;
import com.east2west.models.Entity.Car;
import com.east2west.models.Entity.Rental;
import com.east2west.service.*;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
@RestController
@RequestMapping("/api/rental")
public class RentalCarController {
    @Autowired
    private RentalCarService rentalCarService;

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
}
