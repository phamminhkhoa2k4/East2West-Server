package com.east2west.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.context.Theme;
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
import com.east2west.models.Entity.CategoryTour;
import com.east2west.models.Entity.DepartureDate;
import com.east2west.models.Entity.SuitableTour;
import com.east2west.models.Entity.ThemeTour;
import com.east2west.models.Entity.TourCategoryTour;
import com.east2west.models.Entity.TourPackage;
import com.east2west.models.Entity.TourSuitableTour;
import com.east2west.service.PackTourService;

// @CrossOrigin(origins = "*", maxAge = 3600)
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

    
    @GetMapping("/category/{categoryTourName}")
    public List<TourPackage> getToursByCategory(@PathVariable String categoryTourName) {
        return  packTourService.getAllTourPackagesByCategory(categoryTourName);
    }

    @GetMapping("/theme/{themeTourName}")
    public List<TourPackage> getToursByThemeTourName(@RequestParam String themeTourName) {
        return packTourService.getToursByThemeTourName(themeTourName);
    }
    @GetMapping("/category")
    public List<CategoryTour> getAllTourPackagesCategory() {
        return packTourService.getAllTourPackagesCategory();
    }
    @GetMapping("/theme")
    public List<ThemeTour> getAllTheme() {
        return packTourService.getAllTourPackagesTheme();
    }
    @GetMapping("/suitable")
    public List<SuitableTour> getAllToursSuitable() {
        return packTourService.getAllTourPackagesSuitable();
    }
    @GetMapping("/departuredate")
    public List<DepartureDate> getAllToursDepartureDate() {
        return packTourService.getAllDepartureDate();
    }
    @GetMapping("/top10")
    public List<TourPackage> getTop10Tours() {
        return packTourService.findTop10ByOrderByTotalBookingsDesc();
    }

    // Endpoint to get the top tours for the current month based on bookings
    // @GetMapping("/top-by-month")
    // public ResponseEntity<List<TourPackage>> getTopToursByCurrentMonth() {
    //     List<TourPackage> topTours = packTourService.getTopToursByCurrentMonth();
    //     return ResponseEntity.ok(topTours);
    // }
}