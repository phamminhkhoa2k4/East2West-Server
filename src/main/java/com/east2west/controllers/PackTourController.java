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
import com.east2west.models.Entity.Category;
import com.east2west.models.Entity.DepartureDate;
import com.east2west.models.Entity.Suitable;
import com.east2west.models.Entity.Theme;
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
        return packTourService.getAllTourpackages();
    }

    @GetMapping("/{packageid}")
    // @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public TourPackageDetailDTO getToursByPackageId(@PathVariable int packageid) {
        return packTourService.getTourDetailByPackageid(packageid);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<TourPackage>> searchTourPackages(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) Integer minPrice,
        @RequestParam(required = false) Integer maxPrice,
        @RequestParam(required = false) Integer categoryId,
        @RequestParam(required = false) Integer themeId,
        @RequestParam(required = false) Integer suitableId
    ) {
        List<TourPackage> result = packTourService.searchTourPackages(title, minPrice, maxPrice, categoryId, themeId, suitableId);
        return ResponseEntity.ok(result);
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
    public List<Category> getAllTourPackagesCategory() {
        return packTourService.getAllTourPackagesCategory();
    }

    @GetMapping("/theme")
    public List<Theme> getAllTheme() {
        return packTourService.getAllTourPackagesTheme();
    }

    /*@GetMapping("/suitable")
    public List<Suitable> getAllToursSuitable() {
        return packTourService.getAllTourPackagesSuitable();
    }*/

    @GetMapping("/departuredate")
    public List<DepartureDate> getAllToursDepartureDate() {
        return packTourService.getAllDepartureDate();
    }

    @GetMapping("/top10")
    public List<TourPackage> getTop10Tours() {
        return packTourService.findTop10ByOrderByTotalBookingsDesc();
    }

    @PostMapping("/category")
    public ResponseEntity<Category> createCategoryTour(@RequestBody Category categoryTour) {
        Category createdCategory = packTourService.createCategoryTour(categoryTour);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PostMapping("/theme")
    public ResponseEntity<Theme> createThemeTour(@RequestBody Theme themeTour) {
        Theme createdTheme = packTourService.createThemeTour(themeTour);
        return new ResponseEntity<>(createdTheme, HttpStatus.CREATED);
    }

//    @PostMapping("/suitable")
//public ResponseEntity<Suitable> createSuitableTour(@RequestBody Suitable suitableTour) {
//    try {
//        Suitable createdSuitable = packTourService.saveSuitableTour(suitableTour);
//        return new ResponseEntity<>(createdSuitable, HttpStatus.CREATED);
//    } catch (IllegalArgumentException e) {
//        return ResponseEntity.badRequest().body(null);
//    }
//}

//    @GetMapping("/suitable/{id}")
//    public Optional<Suitable> getSuitableById(@PathVariable int id) {
//        return packTourService.findSuitableById(id);
//    }


    @GetMapping("/category/{id}")
    public  Optional<Category> getCategoryById(@PathVariable int id) {
       return packTourService.findCategoryById(id);

    }
    
    @GetMapping("/theme/{id}")
    public Optional<Theme>getThemeById(@PathVariable int id) {
         return packTourService.findThemeById(id);
    }


    @PutMapping("/suitable/{id}")
public ResponseEntity<ApiResponse<Suitable>> updateSuitableTour(
        @PathVariable int id,
        @RequestBody Suitable suitableTourData) {
    Optional<Suitable> existingSuitableTour = packTourService.findSuitableById(id);
    if (existingSuitableTour.isPresent()) {
        Suitable updatedSuitableTour = existingSuitableTour.get();
        updatedSuitableTour.setSuitableName(suitableTourData.getSuitableName()); // Update with new data
        packTourService.saveSuitableTour(updatedSuitableTour);
        ApiResponse<Suitable> response = new ApiResponse<>(updatedSuitableTour, "success", null);
        return ResponseEntity.ok(response);
    } else {
        ApiResponse<Suitable> response = new ApiResponse<>(null, "error", "SuitableTour not found for update.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

// Update CategoryTour
@PutMapping("/category/{id}")
public ResponseEntity<ApiResponse<Category>> updateCategory(
        @PathVariable int id,
        @RequestBody Category categoryTourData) {
    Optional<Category> existingCategory = packTourService.findCategoryById(id);
    if (existingCategory.isPresent()) {
        Category updatedCategory = existingCategory.get();
        updatedCategory.setCategoryName(categoryTourData.getCategoryName()); // Update with new data
        packTourService.saveCategory(updatedCategory);
        ApiResponse<Category> response = new ApiResponse<>(updatedCategory, "success", null);
        return ResponseEntity.ok(response);
    } else {
        ApiResponse<Category> response = new ApiResponse<>(null, "error", "CategoryTour not found for update.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

// Update ThemeTour
@PutMapping("/theme/{id}")
public ResponseEntity<ApiResponse<Theme>> updateTheme(
        @PathVariable int id,
        @RequestBody Theme themeTourData) {
    Optional<Theme> existingTheme = packTourService.findThemeById(id);
    if (existingTheme.isPresent()) {
        Theme updatedTheme = existingTheme.get();
        updatedTheme.setThemeName(themeTourData.getThemeName()); // Update with new data
        packTourService.saveTheme(updatedTheme);
        ApiResponse<Theme> response = new ApiResponse<>(updatedTheme, "success", null);
        return ResponseEntity.ok(response);
    } else {
        ApiResponse<Theme> response = new ApiResponse<>(null, "error", "ThemeTour not found for update.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
//    @PostMapping("/filter")
//    public ResponseEntity<List<TourPackage>> filterTourPackages(@RequestBody TourPackageFilterDTO filterDTO) {
//        List<TourPackage> filteredPackages = packTourService.filterTourPackages(filterDTO);
//        return ResponseEntity.ok(filteredPackages);
//    }
    @GetMapping("/search/name")
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