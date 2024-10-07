package com.east2west.controllers.admin;

import java.util.List;
import java.util.Optional;

import com.east2west.models.DTO.TourPackageDTO;
import com.east2west.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.east2west.models.Entity.TourPackage;
import com.east2west.service.PackTourService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tours/admin")
public class AdminPackTourController {

    @Autowired
    private PackTourService packTourService;





    @GetMapping
    public ResponseEntity<List<TourPackage>> getAllTours() {
        List<TourPackage> tours = packTourService.getAllTourpackages();
        return new ResponseEntity<>(tours,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TourPackage> getTourById(@PathVariable int id) {
        TourPackage tour = packTourService.getTourPackageByPackageid(id);
        return tour != null ? new ResponseEntity<>(tour, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<TourPackage> createTour(@RequestBody TourPackageDTO tourPackageDTO) {
        TourPackage createdTour = packTourService.createTour(tourPackageDTO);
        return new ResponseEntity<>(createdTour, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTourPackage(@PathVariable int id,
            @RequestBody @Valid TourPackageDTO tourPackageDTO) {

        Optional<TourPackage> existingTourPackageOpt = packTourService.findById(id);

        if (existingTourPackageOpt.isEmpty()) {
            return new ResponseEntity<>("TourPackage not found", HttpStatus.NOT_FOUND);
        }


        if (tourPackageDTO.getTitle() == null || tourPackageDTO.getTitle().trim().isEmpty()) {
            return new ResponseEntity<>("Title cannot be empty", HttpStatus.BAD_REQUEST);
        }


        boolean isTitleDuplicate = false;
        if (!existingTourPackageOpt.get().getTitle().equals(tourPackageDTO.getTitle())) {
            isTitleDuplicate = packTourService.existsByTitle(tourPackageDTO.getTitle());
        }

        if (isTitleDuplicate) {
            return new ResponseEntity<>("Title already exists", HttpStatus.CONFLICT);
        }


        TourPackage updatedTourPackage = packTourService.updateTourPackageFields(existingTourPackageOpt.get(),
                tourPackageDTO);
        packTourService.save(updatedTourPackage);

        return new ResponseEntity<>(updatedTourPackage, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable int id) {
        boolean isDeleted = packTourService.deleteTour(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/theme/{id}")
    public ResponseEntity<String> deleteThemeTour(@PathVariable int id) {
        try {
            boolean isDeleted = packTourService.deleteThemeTour(id);
            return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>(
                    "Cannot delete theme tour: This theme is referenced by other records. Please handle those dependencies first.",
                    HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<String> deleteCategoryTour(@PathVariable int id) {
        try {
            boolean isDeleted = packTourService.deleteCategoryTour(id);
            return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>(
                    "Cannot delete category tour: This category is referenced by other records. Please handle those dependencies first.",
                    HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/suitable/{id}")
    public ResponseEntity<String> deleteSuitableTour(@PathVariable int id) {
        try {
            boolean isDeleted = packTourService.deleteSuitable(id);
            return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>(
                    "Cannot delete suitable tour: This suitability option is referenced by other records. Please handle those dependencies first.",
                    HttpStatus.CONFLICT);
        }
    }
}
