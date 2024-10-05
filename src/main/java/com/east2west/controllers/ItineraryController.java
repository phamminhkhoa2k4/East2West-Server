package com.east2west.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.east2west.models.DTO.AccommodationDTO;
import com.east2west.models.DTO.ItineraryDTO;
import com.east2west.models.DTO.MealDTO;
import com.east2west.models.DTO.PlaceDTO;
import com.east2west.models.DTO.TransferDTO;
import com.east2west.models.Entity.Accommodation;
import com.east2west.models.Entity.Itinerary;
import com.east2west.models.Entity.Meal;
import com.east2west.models.Entity.Place;
import com.east2west.models.Entity.Transfer;
import com.east2west.service.ItineraryService;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<Itinerary> createOrUpdateItinerary(@RequestBody ItineraryDTO itineraryDTO) {
        Itinerary itinerary = itineraryService.createOrUpdateItinerary(itineraryDTO);
        return ResponseEntity.ok(itinerary);
    }
    @PostMapping("/")
    public ResponseEntity<List<Itinerary>> createItinerary(@RequestBody List<ItineraryDTO> itineraryDTO) {
        List<Itinerary> itinerary = itineraryService.createItinerary(itineraryDTO);
        return ResponseEntity.ok(itinerary);
    }
    @PutMapping("/")
    public ResponseEntity<Itinerary> updateItinerary(@RequestBody ItineraryDTO itineraryDTO) {
        Itinerary itinerary = itineraryService.updateItinerary(itineraryDTO);
        return ResponseEntity.ok(itinerary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItinerary(@PathVariable int id) {
        itineraryService.deleteItinerary(id);
        return ResponseEntity.noContent().build();
    }

    // Accommodation Endpoints
    @PostMapping("/accommodations")
    public ResponseEntity<Accommodation> createAccommodation(@RequestBody AccommodationDTO accommodationDTO) {
        Accommodation accommodation = itineraryService.createAccommodation(accommodationDTO);
        return ResponseEntity.ok(accommodation);
    }

    @PutMapping("/accommodations")
    public ResponseEntity<Accommodation> updateAccommodation(@RequestBody AccommodationDTO accommodationDTO) {
        Accommodation accommodation = itineraryService.updateAccommodation(accommodationDTO);
        return ResponseEntity.ok(accommodation);
    }

    @DeleteMapping("/accommodations/{id}")
    public ResponseEntity<Void> deleteAccommodation(@PathVariable int id) {
        itineraryService.deleteAccommodation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/accommodations/{id}")
    public ResponseEntity<Optional<Accommodation>> getAccommodationById(@PathVariable int id) {
        Optional<Accommodation> accommodation = itineraryService.getAccommodationById(id);
        return ResponseEntity.ok(accommodation);
    }

    @GetMapping("/accommodations/search")
    public ResponseEntity<List<Accommodation>> searchAccommodations(@RequestParam String search) {
        List<Accommodation> accommodations = null;
        if (!Objects.equals(search, "")) {
            accommodations = itineraryService.searchAccommodationsByName(search);
        }

        return ResponseEntity.ok(accommodations);
    }

    @GetMapping("/accommodations")
    public ResponseEntity<List<Accommodation>> getAllAccommodations() {
        List<Accommodation> accommodations = itineraryService.getAllAccommodations();
        return ResponseEntity.ok(accommodations);
    }

    // Place
    @PostMapping("/places")
    public ResponseEntity<Place> createPlace(@RequestBody PlaceDTO placeDTO) {
        Place place = itineraryService.createPlace(placeDTO);
        return ResponseEntity.ok(place);
    }
    @PutMapping("/places")
    public ResponseEntity<Place> updatePlace(@RequestBody PlaceDTO placeDTO) {
        Place place = itineraryService.updatePlace(placeDTO);
        return ResponseEntity.ok(place);
    }


    @DeleteMapping("/places/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable int id) {
        itineraryService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/places/{id}")
    public ResponseEntity<Optional<Place>> getPlaceById(@PathVariable int id) {
        Optional<Place> place = itineraryService.getPlaceById(id);
        return ResponseEntity.ok(place);
    }

    @GetMapping("/places/search")
    public ResponseEntity<List<Place>> searchPlaces(@RequestParam String search) {
        List<Place> places =null;
        if(!Objects.equals(search, "")){
            places= itineraryService.searchPlacesByName(search);
        }
        return ResponseEntity.ok(places);
    }

    @GetMapping("/places")
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = itineraryService.getAllPlaces();
        return ResponseEntity.ok(places);
    }

    // Meal
    @PostMapping("/meals")
    public ResponseEntity<Meal> createMeal(@RequestBody MealDTO mealDTO) {
        Meal meal = itineraryService.createMeal(mealDTO);
        return ResponseEntity.ok(meal);
    }

    @PutMapping("/meals")
    public ResponseEntity<Meal> updateMeal(@RequestBody MealDTO mealDTO) {
        Meal meal = itineraryService.updateMeal(mealDTO);
        return ResponseEntity.ok(meal);
    }

    @DeleteMapping("/meals/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable int id) {
        itineraryService.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/meals/{id}")
    public ResponseEntity<Optional<Meal>> getMealsById(@PathVariable int id) {
        Optional<Meal> meal = itineraryService.getMealsById(id);
        return ResponseEntity.ok(meal);
    }



    @GetMapping("/meals/search")
    public ResponseEntity<List<Meal>> searchMeals(@RequestParam String search) {
        List<Meal> meals = null;
        if(!Objects.equals(search, "")){
            meals=itineraryService.searchMealsByName(search);
        }
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/meals")
    public ResponseEntity<List<Meal>> getAllMeals() {
        List<Meal> meals = itineraryService.getAllMeals();
        return ResponseEntity.ok(meals);
    }



    // Transfer
    @PostMapping("/transfers")
    public ResponseEntity<Transfer> createTransfer(@RequestBody TransferDTO transferDTO) {
        Transfer transfer = itineraryService.createTransfer(transferDTO);
        return ResponseEntity.ok(transfer);
    }

    @PutMapping("/transfers")
    public ResponseEntity<Transfer> updateTransfer(@RequestBody TransferDTO transferDTO) {
        Transfer transfer = itineraryService.updateTransfer(transferDTO);
        return ResponseEntity.ok(transfer);
    }

    @DeleteMapping("/transfers/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable int id) {
        itineraryService.deleteTransfer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<Optional<Transfer>> getTransferById(@PathVariable int id) {
        Optional<Transfer> transfer = itineraryService.getTransferById(id);
        return ResponseEntity.ok(transfer);
    }

    @GetMapping("/transfers/search")
    public ResponseEntity<List<Transfer>> searchTransfers(@RequestParam String search) {
        List<Transfer> transfers = null;
        if(search!=null){
            transfers=itineraryService.searchTransfersByName(search);
        }
        return ResponseEntity.ok(transfers);
    }

    @GetMapping("/transfers")
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        List<Transfer> transfers = itineraryService.getAllTransfers();
        return ResponseEntity.ok(transfers);
    }
}
