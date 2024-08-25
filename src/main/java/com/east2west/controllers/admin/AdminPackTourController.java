package com.east2west.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.east2west.models.DTO.TourPackageDTO;
import com.east2west.models.Entity.TourPackage;
import com.east2west.service.PackTourService;


@RestController
@RequestMapping("/api/tours/admin")
public class AdminPackTourController {

    @Autowired
    private PackTourService packTourService;


    @GetMapping
    public ResponseEntity<List<TourPackage>> getAllTours() {
        List<TourPackage> tours = packTourService.getAllTourPackages();
        return new ResponseEntity<>(tours, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPackage> getTourById(@PathVariable int id) {
        TourPackage tour = packTourService.getTourPackageByPackageid(id);
        return tour != null ? new ResponseEntity<>(tour, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


   @PostMapping
    public ResponseEntity<TourPackage> createTour(@RequestBody TourPackageDTO tourPackageDTO) {
        TourPackage createdTour = packTourService.createOrUpdateTour(tourPackageDTO);
        return new ResponseEntity<>(createdTour, HttpStatus.CREATED);
    }
    // {
    //     "id":null,
    //     "title": "Adventure in the Mountainssss",
    //     "thumbnail": "https://example.com/images/mountain-adventure.jpg",
    //     "price": 1999.99,
    //     "pricereduce": 1799.99,
    //     "groupsize": "10",
    //     "deposit": "500",
    //     "bookinghold": "48 hours",
    //     "bookingchange": "No fee up to 7 days before departure",
    //     "categoryTourId": [1, 2],
    //     "themeTourId": [1, 3],
    //     "suitableTourId": [2],
    //     "departureDateDate": [
    //         "2024-09-01T08:00:00Z",
    //         "2024-09-15T08:00:00Z",
    //         "2024-10-01T08:00:00Z"
    //     ]
    // }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable int id) {
        boolean isDeleted = packTourService.deleteTour(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}   
