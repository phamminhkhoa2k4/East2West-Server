package com.east2west.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.east2west.models.DTO.TourPackageDTO;
import com.east2west.models.DTO.TourPackageDetailDTO;
import com.east2west.models.Entity.TourCategoryTour;
import com.east2west.models.Entity.TourPackage;
import com.east2west.service.PackTourService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/tours")
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


//    @PostMapping
//     public ResponseEntity<TourPackage> createTour(@RequestBody TourPackageDTO tourPackageDTO) {
//         TourPackage createdTour = packTourService.createTour(tourPackageDTO);
//         return new ResponseEntity<>(createdTour, HttpStatus.CREATED);
//     }

    @PutMapping("/{id}")
    public ResponseEntity<TourPackage> updateTour(@PathVariable int id, @RequestBody TourPackageDTO tourPackageDTO) {
        TourPackage updatedTour = packTourService.updateTour(id, tourPackageDTO);
        return updatedTour != null ? new ResponseEntity<>(updatedTour, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

   
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable int id) {
        boolean isDeleted = packTourService.deleteTour(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
