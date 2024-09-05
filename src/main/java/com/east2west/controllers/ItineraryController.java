package com.east2west.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    // {
    //     "itineraryId": null,  // Optional, use if updating
    //tourPackageId:1
    //     "name": "Northern Lights Tour",
    //     "description": "A 5-day tour to explore the Northern Lights",
    //     "accommodationIds": [1, 2],  // List of accommodation IDs
    //     "mealIds": [1, 3],  // List of meal IDs
    //     "placeIds": [4, 5]  // List of place IDs
    // }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItinerary(@PathVariable int id) {
        itineraryService.deleteItinerary(id);
        return ResponseEntity.noContent().build();
    }

    // Accommodation Endpoints
    @PostMapping("/accommodations/createOrUpdate")
    public ResponseEntity<Accommodation> createOrUpdateAccommodation(@RequestBody AccommodationDTO accommodationDTO) {
        Accommodation accommodation = itineraryService.createOrUpdateAccommodation(accommodationDTO);
        return ResponseEntity.ok(accommodation);
    }

    @DeleteMapping("/accommodations/{id}")
    public ResponseEntity<Void> deleteAccommodation(@PathVariable int id) {
        itineraryService.deleteAccommodation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/accommodations/search")
    public ResponseEntity<List<Accommodation>> searchAccommodations(@RequestParam(required = false, defaultValue = "") String search) {
        List<Accommodation> accommodations = itineraryService.searchAccommodationsByName(search);
        return ResponseEntity.ok(accommodations);
    }

    @GetMapping("/accommodations")
    public ResponseEntity<List<Accommodation>> getAllAccommodations() {
        List<Accommodation> accommodations = itineraryService.getAllAccommodations();
        return ResponseEntity.ok(accommodations);
    }

    // Place Endpoints
    @PostMapping("/places/createOrUpdate")
    public ResponseEntity<Place> createOrUpdatePlace(@RequestBody PlaceDTO placeDTO) {
        Place place = itineraryService.createOrUpdatePlace(placeDTO);
        return ResponseEntity.ok(place);
    }

    @DeleteMapping("/places/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable int id) {
        itineraryService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/places/search")
    public ResponseEntity<List<Place>> searchPlaces(@RequestParam(required = false, defaultValue = "") String search) {
        List<Place> places = itineraryService.searchPlacesByName(search);
        return ResponseEntity.ok(places);
    }

    @GetMapping("/places")
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = itineraryService.getAllPlaces();
        return ResponseEntity.ok(places);
    }

    // Meal Endpoints
    @PostMapping("/meals/createOrUpdate")
    public ResponseEntity<Meal> createOrUpdateMeal(@RequestBody MealDTO mealDTO) {
        Meal meal = itineraryService.createOrUpdateMeal(mealDTO);
        return ResponseEntity.ok(meal);
    }

    @DeleteMapping("/meals/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable int id) {
        itineraryService.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/meals/search")
    public ResponseEntity<List<Meal>> searchMeals(@RequestParam(required = false, defaultValue = "") String search) {
        List<Meal> meals = itineraryService.searchMealsByName(search);
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/meals")
    public ResponseEntity<List<Meal>> getAllMeals() {
        List<Meal> meals = itineraryService.getAllMeals();
        return ResponseEntity.ok(meals);
    }

    // Transfer Endpoints
    @PostMapping("/transfers/createOrUpdate")
    public ResponseEntity<Transfer> createOrUpdateTransfer(@RequestBody TransferDTO transferDTO) {
        Transfer transfer = itineraryService.createOrUpdateTransfer(transferDTO);
        return ResponseEntity.ok(transfer);
    }

    @DeleteMapping("/transfers/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable int id) {
        itineraryService.deleteTransfer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/transfers/search")
    public ResponseEntity<List<Transfer>> searchTransfers(@RequestParam(required = false, defaultValue = "") String search) {
        List<Transfer> transfers = itineraryService.searchTransfersByName(search);
        return ResponseEntity.ok(transfers);
    }

    @GetMapping("/transfers")
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        List<Transfer> transfers = itineraryService.getAllTransfers();
        return ResponseEntity.ok(transfers);
    }
}
