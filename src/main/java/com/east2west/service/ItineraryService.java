package com.east2west.service;

import com.east2west.models.Entity.*;
import com.east2west.exception.ResourceNotFoundException;
import com.east2west.models.DTO.AccommodationDTO;
import com.east2west.models.DTO.ItineraryDTO;
import com.east2west.models.DTO.MealDTO;
import com.east2west.models.DTO.PlaceDTO;
import com.east2west.models.DTO.TransferDTO;
import com.east2west.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private TransferRepository transferRepository;

    public Itinerary createOrUpdateItinerary(ItineraryDTO itineraryDTO) {
        Optional<Itinerary> optionalItinerary = itineraryRepository.findById(itineraryDTO.getItineraryId());
        Itinerary itinerary;

        if (optionalItinerary.isPresent()) {
            itinerary = optionalItinerary.get();

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
            itinerary.setAccommodations(new ArrayList<>());
            itinerary.setMeals(new ArrayList<>());
            itinerary.setPlaces(new ArrayList<>());
        }

        Optional<TourPackage> optionalTourPackage = tourPackageRepository.findById(itineraryDTO.getTourPackageId());
        optionalTourPackage.ifPresent(itinerary::setTourPackage);

        List<Accommodation> accommodations = accommodationRepository.findAllById(itineraryDTO.getAccommodationIds());
        itinerary.getAccommodations().clear();
        itinerary.getAccommodations().addAll(accommodations);

        List<Meal> meals = mealRepository.findAllById(itineraryDTO.getMealIds());
        itinerary.getMeals().clear();
        itinerary.getMeals().addAll(meals);

        List<Place> places = placeRepository.findAllById(itineraryDTO.getPlaceIds());
        itinerary.getPlaces().clear();
        itinerary.getPlaces().addAll(places);

        itinerary.setDay(itineraryDTO.getDay());

        return itineraryRepository.save(itinerary);
    }

    public void deleteItinerary(int id) {
        itineraryRepository.deleteById(id);
    }

    public Accommodation createOrUpdateAccommodation(AccommodationDTO accommodationDTO) {
        Accommodation accommodation;

        if (accommodationDTO.getAccommodationid() > 0) {
            Optional<Accommodation> existingAccommodation = accommodationRepository
                    .findById(accommodationDTO.getAccommodationid());
            accommodation = existingAccommodation.get();

        } else {
            if (accommodationRepository.existsByAccommodationname(accommodationDTO.getAccommodationname())) {
                throw new IllegalArgumentException("Accommodation with this name already exists.");
            }
            accommodation = new Accommodation();
        }

        accommodation.setAccommodationname(accommodationDTO.getAccommodationname());
        accommodation.setDurationaccommodation(accommodationDTO.getDurationaccommodation());
        accommodation.setAccommodationtype(accommodationDTO.getAccommodationtype());

        return accommodationRepository.save(accommodation);
    }

    public void deleteAccommodation(int id) {
        accommodationRepository.deleteById(id);
    }

    public List<Accommodation> searchAccommodationsByName(String name) {
        return accommodationRepository.findByAccommodationnameContainingIgnoreCase(name);
    }

    public List<Accommodation> getAllAccommodations() {
        return accommodationRepository.findAll();
    }
    public Meal createOrUpdateMeal(MealDTO mealDTO) {
        Meal meal;
        if (mealDTO.getMealid() > 0) {
            Optional<Meal> existingMeal = mealRepository.findById(mealDTO.getMealid());
            if (existingMeal.isPresent()) {
                meal = existingMeal.get();
            } else {
                throw new ResourceNotFoundException("Meal not found with id " + mealDTO.getMealid());
            }
        } else {
            if (mealRepository.existsByMealname(mealDTO.getMealname())) {
                throw new IllegalArgumentException("Meal with this name already exists.");
            }
            meal = new Meal();
        }
        meal.setMealname(mealDTO.getMealname());
        meal.setMealthumbnail(mealDTO.getMealthumbnail());
        meal.setMealduration(mealDTO.getMealduration());
        meal.setMealactivity(mealDTO.getMealactivity());

        return mealRepository.save(meal);
    }

    public void deleteMeal(int id) {
        mealRepository.deleteById(id);
    }

    public List<Meal> searchMealsByName(String name) {
        return mealRepository.findByMealnameContainingIgnoreCase(name);
    }

    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    public Place createOrUpdatePlace(PlaceDTO placeDTO) {
        Place place;

        if (placeDTO.getPlaceid() > 0) {
            Optional<Place> existingPlace = placeRepository.findById(placeDTO.getPlaceid());
            if (existingPlace.isPresent()) {
                place = existingPlace.get();
            } else {
                throw new ResourceNotFoundException("Place not found with id " + placeDTO.getPlaceid());
            }
        } else {
            if (placeRepository.existsByPlacename(placeDTO.getPlacename())) {
                throw new IllegalArgumentException("Place with this name already exists.");
            }
            place = new Place();
        }

        place.setPlacename(placeDTO.getPlacename());
        place.setPlacethumbnail(placeDTO.getPlacethumbnail());
        place.setDescription(placeDTO.getDescription());
        place.setPlaceduration(placeDTO.getPlaceduration());

        return placeRepository.save(place);
    }

    public void deletePlace(int id) {
        placeRepository.deleteById(id);
    }

    public List<Place> searchPlacesByName(String name) {
        return placeRepository.findByPlacenameContainingIgnoreCase(name);
    }

    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public Transfer createOrUpdateTransfer(TransferDTO transferDTO) {
        Transfer transfer;

        if (transferDTO.getTransferid() > 0) {
            Optional<Transfer> existingTransfer = transferRepository.findById(transferDTO.getTransferid());
            if (existingTransfer.isPresent()) {
                transfer = existingTransfer.get();
            } else {
                throw new ResourceNotFoundException("Transfer not found with id " + transferDTO.getTransferid());
            }
        } else {
            if (transferRepository.existsByTransfername(transferDTO.getTransfername())) {
                throw new IllegalArgumentException("Transfer with this name already exists.");
            }
            transfer = new Transfer();
        }

        transfer.setTransfername(transferDTO.getTransfername());
        transfer.setDescription(transferDTO.getDescription());
        transfer.setTransferduration(transferDTO.getTransferduration());

        return transferRepository.save(transfer);
    }

    public void deleteTransfer(int id) {
        transferRepository.deleteById(id);
    }

    public List<Transfer> searchTransfersByName(String name) {
        return transferRepository.findByTransfernameContainingIgnoreCase(name);
    }

    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }
}
