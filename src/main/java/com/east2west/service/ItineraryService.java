package com.east2west.service;

import com.east2west.models.Entity.*;
import com.east2west.exception.ResourceNotFoundException;
import com.east2west.models.DTO.AccommodationDTO;
import com.east2west.models.DTO.ItineraryDTO;
import com.east2west.models.DTO.MealDTO;
import com.east2west.models.DTO.PlaceDTO;
import com.east2west.repository.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ItineraryService {
    
    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private TourPackageRepository tourPackageRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private PlaceRepository placeRepository;

    public Itinerary createOrUpdateItinerary(ItineraryDTO itineraryDTO) {
    Optional<Itinerary> optionalItinerary = itineraryRepository.findById(itineraryDTO.getItineraryId());
    Itinerary itinerary;

    if (optionalItinerary.isPresent()) {
        itinerary = optionalItinerary.get();
        
        // Khởi tạo danh sách nếu chúng là null
        if (itinerary.getAccommodations() == null) {
            itinerary.setAccommodations(new ArrayList<>());
        }
        if (itinerary.getMeals() == null) {
            itinerary.setMeals(new ArrayList<>());
        }
        if (itinerary.getPlaces() == null) {
            itinerary.setPlaces(new ArrayList<>());
        }
    } else {
        itinerary = new Itinerary();
        // Khởi tạo danh sách nếu là tạo mới
        itinerary.setAccommodations(new ArrayList<>());
        itinerary.setMeals(new ArrayList<>());
        itinerary.setPlaces(new ArrayList<>());
    }

    // Cập nhật TourPackage nếu có
    Optional<TourPackage> optionalTourPackage = tourPackageRepository.findById(itineraryDTO.getTourPackageId());
    optionalTourPackage.ifPresent(itinerary::setTourPackage);

    // Cập nhật danh sách accommodations
    List<Accommodation> accommodations = accommodationRepository.findAllById(itineraryDTO.getAccommodationIds());
    itinerary.getAccommodations().clear();
    itinerary.getAccommodations().addAll(accommodations);

    // Cập nhật danh sách meals
    List<Meal> meals = mealRepository.findAllById(itineraryDTO.getMealIds());
    itinerary.getMeals().clear();
    itinerary.getMeals().addAll(meals);

    // Cập nhật danh sách places
    List<Place> places = placeRepository.findAllById(itineraryDTO.getPlaceIds());
    itinerary.getPlaces().clear();
    itinerary.getPlaces().addAll(places);

    // Cập nhật các thuộc tính khác
    itinerary.setDay(itineraryDTO.getDay());

    return itineraryRepository.save(itinerary);
}

    public void deleteItinerary(int id) {
       itineraryRepository.deleteById(id);
    }
    public Accommodation createOrUpdateAccommodation(AccommodationDTO accommodationDTO) {
        Accommodation accommodation = new Accommodation();
        if (accommodationDTO.getAccommodationId() != 0) {
            accommodation = accommodationRepository.findById(accommodationDTO.getAccommodationId())
                    .orElse(new Accommodation());
        }
        // Set fields from DTO to entity
        accommodation.setAccommodationname(accommodationDTO.getAccommodationName());
        accommodation.setDurationaccommodation(accommodationDTO.getDurationAccommodation());
        accommodation.setAccommodationtype(accommodationDTO.getAccommodationType());
        
        return accommodationRepository.save(accommodation);
    }
    
    public void deleteAccommodation(int id) {
        accommodationRepository.deleteById(id);
    }
    
    public List<Accommodation> getAllAccommodations() {
        return accommodationRepository.findAll();
    }
    
    public Accommodation getAccommodationById(int id) {
        return accommodationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id " + id));
    }
    public Meal createOrUpdateMeal(MealDTO mealDTO) {
    Meal meal = new Meal();
    if (mealDTO.getMealId() != 0) {
        meal = mealRepository.findById(mealDTO.getMealId())
                .orElse(new Meal());
    }
    // Set fields from DTO to entity
    meal.setMealname(mealDTO.getMealName());
    meal.setMealthumbnail(mealDTO.getMealThumbnail());
    meal.setMealduration(mealDTO.getMealDuration());
    meal.setMealactivity(mealDTO.getMealActivity());
    
    return mealRepository.save(meal);
}

public void deleteMeal(int id) {
    mealRepository.deleteById(id);
}

public List<Meal> getAllMeals() {
    return mealRepository.findAll();
}

public Meal getMealById(int id) {
    return mealRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id " + id));
}
public Place createOrUpdatePlace(PlaceDTO placeDTO) {
    Place place = new Place();
    if (placeDTO.getPlaceId() != 0) {
        place = placeRepository.findById(placeDTO.getPlaceId())
                .orElse(new Place());
    }
    // Set fields from DTO to entity
    place.setPlacename(placeDTO.getPlaceName());
    place.setPlacethumbnail(placeDTO.getPlaceThumbnail());
    place.setDescription(placeDTO.getDescription());
    place.setPlaceduration(placeDTO.getPlaceDuration());
    
    return placeRepository.save(place);
}

public void deletePlace(int id) {
    placeRepository.deleteById(id);
}

public List<Place> getAllPlaces() {
    return placeRepository.findAll();
}

public Place getPlaceById(int id) {
    return placeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Place not found with id " + id));
}

}
