package com.east2west.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.east2west.models.DTO.CategoryTourDTO;
import com.east2west.models.DTO.DepartureDateDetailDTO;
import com.east2west.models.DTO.ItineraryDTO;
import com.east2west.models.DTO.SuitableTourDTO;
import com.east2west.models.DTO.TourCategoryTourDTO;
import com.east2west.models.DTO.TourDepartureDateDTO;
import com.east2west.models.DTO.TourPackageDTO;
import com.east2west.models.DTO.TourPackageDetailDTO;
import com.east2west.models.DTO.TourSuitableTourDTO;
import com.east2west.models.DTO.ThemeTourDTO;
import com.east2west.models.DTO.ThemeTourDetailDTO;
import com.east2west.models.Entity.CategoryTour;
import com.east2west.models.Entity.DepartureDate;
import com.east2west.models.Entity.Itinerary;
import com.east2west.models.Entity.SuitableTour;
import com.east2west.models.Entity.ThemeTour;
import com.east2west.models.Entity.TourCategoryTour;
import com.east2west.models.Entity.TourDepartureDate;
import com.east2west.models.Entity.TourPackage;
import com.east2west.models.Entity.DepartureDate;
// import com.east2west.models.Entity.TourResponse;
import com.east2west.models.Entity.TourSuitableTour;
import com.east2west.models.Entity.TourThemeTour;
import com.east2west.repository.CategoryTourRepository;
import com.east2west.repository.DepartureDateRepository;
import com.east2west.repository.SuitableTourRepository;
import com.east2west.repository.ThemeTourRepository;
import com.east2west.repository.TourCategoryTourRepository;
import com.east2west.repository.TourDepartureDateRepository;
import com.east2west.repository.TourPackageRepository;
import com.east2west.repository.TourSuitableTourRepository;
import com.east2west.repository.TourThemeTourRepository;


@Service
public class PackTourService {
    @Autowired
    private TourPackageRepository tourPackageRepository;

    @Autowired
    private TourCategoryTourRepository tourCategoryTourRepository;

    @Autowired
    private TourThemeTourRepository tourThemeTourRepository;

    @Autowired
    private TourDepartureDateRepository tourDepartureDateRepository;

    @Autowired
    private TourSuitableTourRepository tourSuitableTourRepository;

    @Autowired
    private CategoryTourRepository categoryTourRepository;

    @Autowired
    private ThemeTourRepository themeTourRepository;

    @Autowired
    private SuitableTourRepository suitableTourRepository;

    @Autowired
    private DepartureDateRepository departureDateRepository;

    public List<TourPackage> getAllTourPackages() {
        return tourPackageRepository.findAll();
    }

    public List<TourCategoryTour> getAllTourPackagesCategory() {
        return tourCategoryTourRepository.findAll();
    }

    public List<TourPackage> getAllTourPackagesByCategory(String cat) {
        return tourPackageRepository.findByCategoryTourName(cat);
    }

    public List<TourThemeTour> getAllTourPackagesTheme() {
        return tourThemeTourRepository.findAll();
    }

    public List<TourPackage> getToursByThemeTourName(String themeTourName) {
        return tourPackageRepository.findByThemeTourName(themeTourName);
    }

    public List<TourDepartureDate> getAllTourPackagesDepartureDate() {
        return tourDepartureDateRepository.findAll();
    }

    public List<TourSuitableTour> getAllTourPackagesSuitable() {
        return tourSuitableTourRepository.findAll();
    }

    public List<TourPackage> getToursBySuitableName(String suitableName) {
        return tourPackageRepository.findBySuitableName(suitableName);
    }

    public TourPackageDetailDTO getTourDetailByPackageid(int packageid) {
        TourPackage tourPackage = tourPackageRepository.findByPackageid(packageid);
        List<Itinerary> itineraries = tourPackage.getItineraries();

        TourPackageDetailDTO dto = new TourPackageDetailDTO();
        dto.setPackageid(tourPackage.getPackageid());
        dto.setTitle(tourPackage.getTitle());
        dto.setThumbnail(tourPackage.getThumbnail());
        dto.setPrice(tourPackage.getPrice());
        dto.setPricereduce(tourPackage.getPricereduce());
        dto.setGroupsize(tourPackage.getGroupsize());
        dto.setDeposit(tourPackage.getDeposit());
        dto.setBookinghold(tourPackage.getBookinghold());
        dto.setBookingchange(tourPackage.getBookingchange());
        dto.setItineraries(itineraries);
        dto.setCategoryTours(tourPackage.getCategoryTours());
        dto.setThemeTours(tourPackage.getThemeTours());
        dto.setDepartureDates(tourPackage.getDepartureDate());
        dto.setSuitableTours(tourPackage.getSuitableTours());
        return dto;
    }

    public TourPackage getTourPackageByPackageid(int id) {
        return tourPackageRepository.findByPackageid(id);
    }

    // Create a new tour
    public TourPackage createTour(TourPackageDTO tourPackageDTO) {
        TourPackage tourPackage = new TourPackage();
        // Map basic fields
        tourPackage.setTitle(tourPackageDTO.getTitle());
        tourPackage.setThumbnail(tourPackageDTO.getThumbnail());
        tourPackage.setPrice(tourPackageDTO.getPrice());
        tourPackage.setPricereduce(tourPackageDTO.getPricereduce());
        tourPackage.setGroupsize(tourPackageDTO.getGroupsize());
        tourPackage.setDeposit(tourPackageDTO.getDeposit());
        tourPackage.setBookinghold(tourPackageDTO.getBookinghold());
        tourPackage.setBookingchange(tourPackageDTO.getBookingchange());

        List<CategoryTour> categoryTours = categoryTourRepository.findAllById(tourPackageDTO.getCategoryTourId());
        tourPackage.setCategoryTours(categoryTours);
        // Convert themeTourNames to ThemeTour entities
        List<ThemeTour> themeTours = themeTourRepository.findAllById(tourPackageDTO.getThemeTourId());
        tourPackage.setThemeTours(themeTours);
        // Convert suitableTourNames to SuitableTour entities
        List<SuitableTour> suitableTours = suitableTourRepository.findAllById(tourPackageDTO.getSuitableTourId());
        tourPackage.setSuitableTours(suitableTours);
        // Convert departureDates, and create any missing ones
        List<Timestamp> departureDates = tourPackageDTO.getDepartureDateDate();
        List<DepartureDate> existingDepartureDates = departureDateRepository.findByDeparturedateIn(departureDates);
        // Find missing departure dates
        List<Timestamp> existingDates = existingDepartureDates.stream()
                .map(DepartureDate::getDeparturedate)
                .collect(Collectors.toList());
        List<Timestamp> missingDates = departureDates.stream()
                .filter(date -> !existingDates.contains(date))
                .collect(Collectors.toList());
        // Create and save new departure dates
        for (Timestamp missingDate : missingDates) {
            DepartureDate newDepartureDate = new DepartureDate();
            newDepartureDate.setDeparturedate(missingDate);
            departureDateRepository.save(newDepartureDate);
            existingDepartureDates.add(newDepartureDate);
        }
        tourPackage.setDepartureDate(existingDepartureDates);
        return tourPackageRepository.save(tourPackage);
    }
    public TourPackage updateTour(int id, TourPackageDTO tourPackageDTO) {
        Optional<TourPackage> existingTourOpt = tourPackageRepository.findById(id);
        if (existingTourOpt.isPresent()) {
            TourPackage tourPackage = existingTourOpt.get();
            tourPackage.setTitle(tourPackageDTO.getTitle());
            tourPackage.setThumbnail(tourPackageDTO.getThumbnail());
            tourPackage.setPrice(tourPackageDTO.getPrice());
            tourPackage.setPricereduce(tourPackageDTO.getPricereduce());
            tourPackage.setGroupsize(tourPackageDTO.getGroupsize());
            tourPackage.setDeposit(tourPackageDTO.getDeposit());
            tourPackage.setBookinghold(tourPackageDTO.getBookinghold());
            tourPackage.setBookingchange(tourPackageDTO.getBookingchange());
            List<CategoryTour> categoryTours = categoryTourRepository.findAllById(tourPackageDTO.getCategoryTourId());
            tourPackage.setCategoryTours(categoryTours);
            // Convert themeTourNames to ThemeTour entities
            List<ThemeTour> themeTours = themeTourRepository.findAllById(tourPackageDTO.getThemeTourId());
            tourPackage.setThemeTours(themeTours);
            // Convert suitableTourNames to SuitableTour entities
            List<SuitableTour> suitableTours = suitableTourRepository.findAllById(tourPackageDTO.getSuitableTourId());
            tourPackage.setSuitableTours(suitableTours);
            // Convert departureDates, and create any missing ones
            List<Timestamp> departureDates = tourPackageDTO.getDepartureDateDate();
            List<DepartureDate> existingDepartureDates = departureDateRepository.findByDeparturedateIn(departureDates);
            // Find missing departure dates
            List<Timestamp> existingDates = existingDepartureDates.stream()
                    .map(DepartureDate::getDeparturedate)
                    .collect(Collectors.toList());
            List<Timestamp> missingDates = departureDates.stream()
                    .filter(date -> !existingDates.contains(date))
                    .collect(Collectors.toList());
            // Create and save new departure dates
            for (Timestamp missingDate : missingDates) {
                DepartureDate newDepartureDate = new DepartureDate();
                newDepartureDate.setDeparturedate(missingDate);
                departureDateRepository.save(newDepartureDate);
                existingDepartureDates.add(newDepartureDate);
            }
            tourPackage.setDepartureDate(existingDepartureDates);
            tourPackageRepository.save(tourPackage);
        }
        return null;
    }
    // Delete a tour
    public boolean deleteTour(int id) {
        Optional<TourPackage> tourPackage = tourPackageRepository.findById(id);
        if (tourPackage.isPresent()) {
            tourPackageRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
