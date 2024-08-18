package com.east2west.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.east2west.models.DTO.BookingTourDTO;
import com.east2west.models.DTO.TourPackageDetailDTO;
import com.east2west.models.Entity.BookingTour;
import com.east2west.models.Entity.TourCategoryTour;
import com.east2west.models.Entity.TourPackage;
import com.east2west.service.PackTourService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tours")
public class PackTourController {
    @Autowired
    private PackTourService packTourService;

    @GetMapping
    public List<TourPackage> getAllTourPackages() {
        return packTourService.getAllTourPackages();
    }

    @GetMapping("/{packageid}")
    // @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public TourPackageDetailDTO getToursByPackageId(@PathVariable int packageid) {
        return packTourService.getTourDetailByPackageid(packageid);
    }

    @GetMapping("/category")
    public List<TourCategoryTour> getAllTourPackagesCategory() {
        return packTourService.getAllTourPackagesCategory();
    }
    @GetMapping("/category/{categoryTourName}")
    public List<TourPackage> getToursByCategory(@PathVariable String categoryTourName) {
        return  packTourService.getAllTourPackagesByCategory(categoryTourName);
    }

    @GetMapping("/theme/{themeTourName}")
    public List<TourPackage> getToursByThemeTourName(@RequestParam String themeTourName) {
        return packTourService.getToursByThemeTourName(themeTourName);
    }

    @GetMapping("/theme/{suitableName}")
    public List<TourPackage> getToursBySuitableName(@RequestParam String suitableName) {
        return packTourService.getToursBySuitableName(suitableName);
    }
   @PostMapping("/booking")
    public ResponseEntity<BookingTour> createBookingTour(@RequestBody BookingTourDTO bookingTourDTO) {
        BookingTour newBooking = packTourService.createBookingTour(bookingTourDTO);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }
    @PostMapping("/cancel/{bookingTourId}")
    public ResponseEntity<BookingTour> cancelBooking(@PathVariable int bookingTourId) {
        BookingTour cancelledBooking = packTourService.cancelBooking(bookingTourId);
        return new ResponseEntity<>(cancelledBooking, HttpStatus.OK);
    }
}