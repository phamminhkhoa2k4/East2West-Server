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
    public Itinerary updateItinerary(ItineraryDTO itineraryDTO) {
        Optional<Itinerary> optionalItinerary = itineraryRepository.findById(itineraryDTO.getItineraryId());
        if (optionalItinerary.isEmpty()) {
            throw new IllegalArgumentException("Itinerary not found");
        }

        Itinerary itinerary = optionalItinerary.get();

        if (itinerary.getAccommodations() == null) {
            itinerary.setAccommodations(new ArrayList<>());
        }
        if (itinerary.getMeals() == null) {
            itinerary.setMeals(new ArrayList<>());
        }
        if (itinerary.getPlaces() == null) {
            itinerary.setPlaces(new ArrayList<>());
        }

        if (itinerary.getTransfers() == null) {
            itinerary.setTransfers(new ArrayList<>());
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

        List<Transfer> transfers = transferRepository.findAllById(itineraryDTO.getTransferIds());
        itinerary.getPlaces().clear();
        itinerary.getTransfers().addAll(transfers);

        itinerary.setDay(itineraryDTO.getDay());

        return itineraryRepository.save(itinerary);
    }

    public List<Itinerary> createItinerary(List<ItineraryDTO> itineraryDTOs) {
        List<Itinerary> itineraries = new ArrayList<>();

        for (ItineraryDTO itineraryDTO : itineraryDTOs) {
            Itinerary itinerary = new Itinerary();
            itinerary.setAccommodations(new ArrayList<>());
            itinerary.setMeals(new ArrayList<>());
            itinerary.setPlaces(new ArrayList<>());
            itinerary.setTransfers(new ArrayList<>());

            // Tìm kiếm TourPackage
            Optional<TourPackage> optionalTourPackage = tourPackageRepository.findById(itineraryDTO.getTourPackageId());
            optionalTourPackage.ifPresent(itinerary::setTourPackage);

            // Lấy danh sách Accommodation
            List<Accommodation> accommodations = accommodationRepository.findAllById(itineraryDTO.getAccommodationIds());
            itinerary.getAccommodations().addAll(accommodations);

            // Lấy danh sách Meal
            List<Meal> meals = mealRepository.findAllById(itineraryDTO.getMealIds());
            itinerary.getMeals().addAll(meals);

            // Lấy danh sách Place
            List<Place> places = placeRepository.findAllById(itineraryDTO.getPlaceIds());
            itinerary.getPlaces().addAll(places);

            // Lấy danh sách Transfer
            List<Transfer> transfers = transferRepository.findAllById(itineraryDTO.getTransferIds());
            itinerary.getTransfers().addAll(transfers);

            // Thiết lập ngày
            itinerary.setDay(itineraryDTO.getDay());

            // Lưu từng Itinerary vào danh sách
            itineraries.add(itineraryRepository.save(itinerary));
        }

        return itineraries; // Trả về danh sách các Itinerary đã tạo
    }


    public void deleteItinerary(int id) {
        itineraryRepository.deleteById(id);
    }



    public Accommodation createAccommodation(AccommodationDTO accommodationDTO) {
        if (accommodationRepository.existsByAccommodationname(accommodationDTO.getAccommodationname())) {
            throw new IllegalArgumentException("Accommodation with this name already exists.");
        }

        Accommodation accommodation = new Accommodation();
        accommodation.setAccommodationname(accommodationDTO.getAccommodationname());
        accommodation.setDurationaccommodation(accommodationDTO.getDurationaccommodation());
        accommodation.setAccommodationtype(accommodationDTO.getAccommodationtype());
        accommodation.setAccommodationthumbnail(accommodationDTO.getAccommodationthumbnail());
        accommodation.setRoomtype(accommodationDTO.getRoomtype());
        accommodation.setIsbreadkfast(accommodationDTO.isIsbreadkfast());

        return accommodationRepository.save(accommodation);
    }

    public Accommodation updateAccommodation(AccommodationDTO accommodationDTO) {
        Optional<Accommodation> existingAccommodation = accommodationRepository
                .findById(accommodationDTO.getAccommodationid());

        if (existingAccommodation.isEmpty()) {
            throw new ResourceNotFoundException("Accommodation not found with id " + accommodationDTO.getAccommodationid());
        }

        Accommodation accommodation = existingAccommodation.get();
        accommodation.setAccommodationname(accommodationDTO.getAccommodationname());
        accommodation.setDurationaccommodation(accommodationDTO.getDurationaccommodation());
        accommodation.setAccommodationtype(accommodationDTO.getAccommodationtype());

        return accommodationRepository.save(accommodation);
    }

    public Optional<Accommodation> getAccommodationById(int id){
        return accommodationRepository.findById(id);
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


    public Meal createMeal(MealDTO mealDTO) {
        if (mealRepository.existsByMealname(mealDTO.getMealname())) {
            throw new IllegalArgumentException("Meal with this name already exists.");
        }

        Meal meal = new Meal();
        meal.setMealname(mealDTO.getMealname());
        meal.setMealthumbnail(mealDTO.getMealthumbnail());
        meal.setMealduration(mealDTO.getMealduration());
        meal.setMealactivity(mealDTO.getMealactivity());

        return mealRepository.save(meal);
    }

    public Meal updateMeal(MealDTO mealDTO) {
        Optional<Meal> existingMeal = mealRepository.findById(mealDTO.getMealid());
        if (existingMeal.isEmpty()) {
            throw new ResourceNotFoundException("Meal not found with id " + mealDTO.getMealid());
        }

        Meal meal = existingMeal.get();
        meal.setMealname(mealDTO.getMealname());
        meal.setMealthumbnail(mealDTO.getMealthumbnail());
        meal.setMealduration(mealDTO.getMealduration());
        meal.setMealactivity(mealDTO.getMealactivity());

        return mealRepository.save(meal);
    }

    public Optional<Meal> getMealsById(int id){
        return mealRepository.findById(id);
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


    public Place createPlace(PlaceDTO placeDTO) {
        if (placeRepository.existsByPlacename(placeDTO.getPlacename())) {
            throw new IllegalArgumentException("Place with this name already exists.");
        }

        Place place = new Place();
        place.setPlacename(placeDTO.getPlacename());
        place.setPlacethumbnail(placeDTO.getPlacethumbnail());
        place.setDescription(placeDTO.getDescription());
        place.setPlaceduration(placeDTO.getPlaceduration());

        return placeRepository.save(place);
    }


    public Place updatePlace(PlaceDTO placeDTO) {
        Optional<Place> existingPlace = placeRepository.findById(placeDTO.getPlaceid());
        if (existingPlace.isEmpty()) {
            throw new ResourceNotFoundException("Place not found with id " + placeDTO.getPlaceid());
        }

        Place place = existingPlace.get();
        place.setPlacename(placeDTO.getPlacename());
        place.setPlacethumbnail(placeDTO.getPlacethumbnail());
        place.setDescription(placeDTO.getDescription());
        place.setPlaceduration(placeDTO.getPlaceduration());

        return placeRepository.save(place);
    }

    public Optional<Place> getPlaceById(int id){
        return placeRepository.findById(id);
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


    public Transfer createTransfer(TransferDTO transferDTO) {
        if (transferRepository.existsByTransfername(transferDTO.getTransfername())) {
            throw new IllegalArgumentException("Transfer with this name already exists.");
        }

        Transfer transfer = new Transfer();
        transfer.setTransfername(transferDTO.getTransfername());
        transfer.setDescription(transferDTO.getDescription());
        transfer.setTransferduration(transferDTO.getTransferduration());
        transfer.setTransferthumbnail(transferDTO.getTransferthumbnail());

        return transferRepository.save(transfer);
    }

    public Transfer updateTransfer(TransferDTO transferDTO) {
        Optional<Transfer> existingTransfer = transferRepository.findById(transferDTO.getTransferid());
        if (!existingTransfer.isPresent()) {
            throw new ResourceNotFoundException("Transfer not found with id " + transferDTO.getTransferid());
        }

        Transfer transfer = existingTransfer.get();
        transfer.setTransfername(transferDTO.getTransfername());
        transfer.setDescription(transferDTO.getDescription());
        transfer.setTransferduration(transferDTO.getTransferduration());
        transfer.setTransferthumbnail(transferDTO.getTransferthumbnail());

        return transferRepository.save(transfer);
    }

    public Optional<Transfer> getTransferById(int id){
        return transferRepository.findById(id);
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
