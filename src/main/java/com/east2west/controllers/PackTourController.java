package com.east2west.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.east2west.models.DTO.ApiResponse;
import com.east2west.models.DTO.TourPackageDetailDTO;
import com.east2west.models.DTO.TourPackageFilterDTO;
import com.east2west.models.Entity.CategoryTour;
import com.east2west.models.Entity.DepartureDate;
import com.east2west.models.Entity.SuitableTour;
import com.east2west.models.Entity.ThemeTour;
import com.east2west.models.Entity.TourPackage;
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

    // @GetMapping("/category/{categoryTourName}")
    // public List<TourPackage> getToursByCategory(@PathVariable String categoryTourName) {
    //     return packTourService.getAllTourPackagesByCategory(categoryTourName);
    // }

    // @GetMapping("/theme/{themeTourName}")
    // public List<TourPackage> getToursByThemeTourName(@RequestParam String themeTourName) {
    //     return packTourService.getToursByThemeTourName(themeTourName);
    // }

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

    @PostMapping("/category")
    public ResponseEntity<CategoryTour> createCategoryTour(@RequestBody CategoryTour categoryTour) {
        CategoryTour createdCategory = packTourService.createCategoryTour(categoryTour);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PostMapping("/theme")
    public ResponseEntity<ThemeTour> createThemeTour(@RequestBody ThemeTour themeTour) {
        ThemeTour createdTheme = packTourService.createThemeTour(themeTour);
        return new ResponseEntity<>(createdTheme, HttpStatus.CREATED);
    }

    @PostMapping("/suitable")
public ResponseEntity<SuitableTour> createSuitableTour(@RequestBody SuitableTour suitableTour) {
    try {
        SuitableTour createdSuitable = packTourService.saveSuitableTour(suitableTour);
        return new ResponseEntity<>(createdSuitable, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(null);
    }
}

    @GetMapping("/suitable/{id}")
    public Optional<SuitableTour> getSuitableById(@PathVariable int id) {
        return packTourService.findSuitableById(id);
    }


    @GetMapping("/category/{id}")
    public  Optional<CategoryTour> getCategoryById(@PathVariable int id) {
       return packTourService.findCategoryById(id);

    }
    
    @GetMapping("/theme/{id}")
    public Optional<ThemeTour>getThemeById(@PathVariable int id) {
         return packTourService.findThemeById(id);
    }


    @PutMapping("/suitable/{id}")
public ResponseEntity<ApiResponse<SuitableTour>> updateSuitableTour(
        @PathVariable int id,
        @RequestBody SuitableTour suitableTourData) {
    Optional<SuitableTour> existingSuitableTour = packTourService.findSuitableById(id);
    if (existingSuitableTour.isPresent()) {
        SuitableTour updatedSuitableTour = existingSuitableTour.get();
        updatedSuitableTour.setSuitableName(suitableTourData.getSuitableName()); // Update with new data
        packTourService.saveSuitableTour(updatedSuitableTour);
        ApiResponse<SuitableTour> response = new ApiResponse<>(updatedSuitableTour, "success", null);
        return ResponseEntity.ok(response);
    } else {
        ApiResponse<SuitableTour> response = new ApiResponse<>(null, "error", "SuitableTour not found for update.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

// Update CategoryTour
@PutMapping("/category/{id}")
public ResponseEntity<ApiResponse<CategoryTour>> updateCategory(
        @PathVariable int id,
        @RequestBody CategoryTour categoryTourData) {
    Optional<CategoryTour> existingCategory = packTourService.findCategoryById(id);
    if (existingCategory.isPresent()) {
        CategoryTour updatedCategory = existingCategory.get();
        updatedCategory.setCategoryTourName(categoryTourData.getCategoryTourName()); // Update with new data
        packTourService.saveCategory(updatedCategory);
        ApiResponse<CategoryTour> response = new ApiResponse<>(updatedCategory, "success", null);
        return ResponseEntity.ok(response);
    } else {
        ApiResponse<CategoryTour> response = new ApiResponse<>(null, "error", "CategoryTour not found for update.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

// Update ThemeTour
@PutMapping("/theme/{id}")
public ResponseEntity<ApiResponse<ThemeTour>> updateTheme(
        @PathVariable int id,
        @RequestBody ThemeTour themeTourData) {
    Optional<ThemeTour> existingTheme = packTourService.findThemeById(id);
    if (existingTheme.isPresent()) {
        ThemeTour updatedTheme = existingTheme.get();
        updatedTheme.setThemeTourName(themeTourData.getThemeTourName()); // Update with new data
        packTourService.saveTheme(updatedTheme);
        ApiResponse<ThemeTour> response = new ApiResponse<>(updatedTheme, "success", null);
        return ResponseEntity.ok(response);
    } else {
        ApiResponse<ThemeTour> response = new ApiResponse<>(null, "error", "ThemeTour not found for update.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
    @PostMapping("/filter")
    public ResponseEntity<List<TourPackage>> filterTourPackages(@RequestBody TourPackageFilterDTO filterDTO) {
        List<TourPackage> filteredPackages = packTourService.filterTourPackages(filterDTO);
        return ResponseEntity.ok(filteredPackages);
    }
    @GetMapping("/search")
    public List<TourPackage> searchToursByTitle(@RequestParam("title") String title) {
        return packTourService.findByTitle(title);
    }
    // Endpoint to get the top tours for the current month based on bookings
    // @GetMapping("/top-by-month")
    // public ResponseEntity<List<TourPackage>> getTopToursByCurrentMonth() {
    // List<TourPackage> topTours = packTourService.getTopToursByCurrentMonth();
    // return ResponseEntity.ok(topTours);
    // }
}