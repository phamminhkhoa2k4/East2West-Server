package com.east2west.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import com.east2west.exception.ResourceNotFoundException;
import com.east2west.models.DTO.BookingTourDTO;
import com.east2west.models.DTO.TourPackageDTO;
import com.east2west.models.DTO.TourPackageDTO.DepartureDateDTO;
import com.east2west.models.DTO.TourPackageDetailDTO;
import com.east2west.models.DTO.TourPackageFilterDTO;
import com.east2west.models.Entity.BookingTour;
import com.east2west.models.Entity.CategoryTour;
import com.east2west.models.Entity.DepartureDate;
import com.east2west.models.Entity.Itinerary;
import com.east2west.models.Entity.Payment;
import com.east2west.models.Entity.SuitableTour;
import com.east2west.models.Entity.ThemeTour;
import com.east2west.models.Entity.TourDepartureDate;
import com.east2west.models.Entity.TourPackage;
import com.east2west.models.Entity.User;
import com.east2west.repository.BookingTourRepository;
import com.east2west.repository.CategoryTourRepository;
import com.east2west.repository.DepartureDateRepository;
import com.east2west.repository.PaymentRepository;
import com.east2west.repository.SuitableTourRepository;
import com.east2west.repository.ThemeTourRepository;
import com.east2west.repository.TourDepartureDateRepository;
import com.east2west.repository.TourPackageRepository;
import com.east2west.repository.UserRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@Service
public class PackTourService {
    @Autowired
    private TourPackageRepository tourPackageRepository;

    
     
    @Autowired
    private ThemeTourRepository ThemeTourRepository;

   

    @Autowired
    private TourDepartureDateRepository tourDepartureDateRepository;



    @Autowired
    private CategoryTourRepository categoryTourRepository;

    @Autowired
    private ThemeTourRepository themeTourRepository;

    @Autowired
    private SuitableTourRepository suitableTourRepository;

    @Autowired
    private DepartureDateRepository departureDateRepository;

    @Autowired
    private BookingTourRepository bookingTourRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<TourPackage> getAllTourPackages() {
        return tourPackageRepository.findAll();
    }

    public List<CategoryTour> getAllTourPackagesCategory() {
        return categoryTourRepository.findAll();
    }

    public List<TourPackage> getAllTourPackagesByCategory(String cat) {
        return tourPackageRepository.findByCategoryTourName(cat);
    }

    public List<ThemeTour> getAllTourPackagesTheme() {
        return ThemeTourRepository.findAll();
    }

    public List<TourPackage> getToursByThemeTourName(String themeTourName) {
        return tourPackageRepository.findByThemeTourName(themeTourName);
    }

    public List<TourDepartureDate> getAllTourPackagesDepartureDate() {
        return tourDepartureDateRepository.findAll();
    }

    public List<SuitableTour> getAllTourPackagesSuitable() {
        return suitableTourRepository.findAll();
    }

    public List<TourPackage> getToursBySuitableName(String suitableName) {
        return tourPackageRepository.findBySuitableName(suitableName);
    }
    public List<DepartureDate> getAllDepartureDate(){
        return departureDateRepository.findAll();
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

    public TourPackage createOrUpdateTour(TourPackageDTO tourPackageDTO) {
    TourPackage tourPackage;
    Integer id = tourPackageDTO.getId();
    if (id == null || id == 0 || !tourPackageRepository.existsById(id)) {
        tourPackage = new TourPackage();
        tourPackage.setCategoryTours(new ArrayList<>());
        tourPackage.setThemeTours(new ArrayList<>());
        tourPackage.setSuitableTours(new ArrayList<>());
        tourPackage.setDepartureDate(new ArrayList<>());
    } else {
        Optional<TourPackage> existingTourOpt = tourPackageRepository.findById(id);
        if (existingTourOpt.isPresent()) {
            tourPackage = existingTourOpt.get();
            if (tourPackage.getCategoryTours() == null) {
                tourPackage.setCategoryTours(new ArrayList<>());
            }
            if (tourPackage.getThemeTours() == null) {
                tourPackage.setThemeTours(new ArrayList<>());
            }
            if (tourPackage.getSuitableTours() == null) {
                tourPackage.setSuitableTours(new ArrayList<>());
            }
            if (tourPackage.getDepartureDate() == null) {
                tourPackage.setDepartureDate(new ArrayList<>());
            }
        } else {
            throw new ResourceNotFoundException("TourPackage not found with id " + id);
        }
    }

    tourPackage.setTitle(tourPackageDTO.getTitle());
    tourPackage.setThumbnail(tourPackageDTO.getThumbnail());
    tourPackage.setPrice(tourPackageDTO.getPrice());
    tourPackage.setPricereduce(tourPackageDTO.getPricereduce());
    tourPackage.setGroupsize(tourPackageDTO.getGroupsize());
    tourPackage.setDeposit(tourPackageDTO.getDeposit());
    tourPackage.setBookinghold(tourPackageDTO.getBookinghold());
    tourPackage.setBookingchange(tourPackageDTO.getBookingchange());

    // Map CategoryTours
    tourPackage.getCategoryTours().clear();
    List<CategoryTour> categoryTours = categoryTourRepository.findAllById(tourPackageDTO.getCategoryTourId());
    tourPackage.setCategoryTours(categoryTours);

    // Map ThemeTours
    tourPackage.getThemeTours().clear();
    List<ThemeTour> themeTours = themeTourRepository.findAllById(tourPackageDTO.getThemeTourId());
    tourPackage.setThemeTours(themeTours);

    // Map SuitableTours
    tourPackage.getSuitableTours().clear();
    List<SuitableTour> suitableTours = suitableTourRepository.findAllById(tourPackageDTO.getSuitableTourId());
    tourPackage.setSuitableTours(suitableTours);

    // Map DepartureDates
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    tourPackage.getDepartureDate().clear();
    List<DepartureDate> existingDepartureDates = new ArrayList<>();

    for (DepartureDateDTO departureDateDTO : tourPackageDTO.getDepartureDates()) {
        String dt = departureDateDTO.getDateTime();
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dt, formatter);
            Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
            Timestamp timestamp = Timestamp.from(instant);
            Optional<DepartureDate> departureDateOpt = departureDateRepository.findByDeparturedate(timestamp);
            DepartureDate departureDate = departureDateOpt.orElseGet(() -> {
                DepartureDate newDepartureDate = new DepartureDate();
                newDepartureDate.setDeparturedate(timestamp);
                departureDateRepository.save(newDepartureDate);
                return newDepartureDate;
            });
            existingDepartureDates.add(departureDate);
        } catch (DateTimeParseException dtpe) {
            throw new RuntimeException("Invalid date format for departure date: " + dt, dtpe);
        }
    }

    tourPackage.setDepartureDate(existingDepartureDates);
    return tourPackageRepository.save(tourPackage);
}


    public boolean deleteTour(int id) {
        Optional<TourPackage> tourPackage = tourPackageRepository.findById(id);
        if (tourPackage.isPresent()) {
            tourPackageRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public BookingTour createBookingTour(BookingTourDTO bookingTourDTO) {
        BookingTour bookingTour = new BookingTour();
        User user = userRepository.findById(bookingTourDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TourPackage tourPackage = tourPackageRepository.findById(bookingTourDTO.getPackageId())
                .orElseThrow(() -> new RuntimeException("Tour Package not found"));

        Payment payment = paymentRepository.findById(bookingTourDTO.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        bookingTour.setUser(user);
        bookingTour.setTourpackage(tourPackage);
        bookingTour.setPayment(payment);
        bookingTour.setBookingdate(bookingTourDTO.getBookingDate());
        bookingTour.setTourdate(bookingTourDTO.getTourDate());
        bookingTour.setNumberofpeople(bookingTourDTO.getNumberOfPeople());
        bookingTour.setTourprice(bookingTourDTO.getTourPrice());
        bookingTour.setDepositamount(bookingTourDTO.getDepositAmount());
        bookingTour.setStatus(bookingTourDTO.getStatus());
        bookingTour.setRefundamount(bookingTourDTO.getRefundAmount());
        bookingTour.setRefunddate(bookingTourDTO.getRefundDate());
        bookingTour.setReason(bookingTourDTO.getReason());
        bookingTour.setDepositrefund(bookingTourDTO.isDepositRefund());
        return bookingTourRepository.save(bookingTour);
    }

    public String cancelBooking(int bookingTourId) {
        BookingTour bookingTour = bookingTourRepository.findById(bookingTourId)
                .orElseThrow(() -> new ResourceNotFoundException("bookingTourId not found with id " + bookingTourId));

        long daysBeforeTour = ChronoUnit.DAYS.between(LocalDate.now(),
                bookingTour.getTourdate().toLocalDateTime().toLocalDate());

        BigDecimal refundAmount = calculateRefund(bookingTour.getDepositamount(), daysBeforeTour);
        bookingTour.setRefundamount(refundAmount);
        bookingTour.setRefunddate(Timestamp.valueOf(LocalDateTime.now()));
        bookingTour.setStatus("Cancelled");
        bookingTour.setDepositrefund(true);

        bookingTourRepository.save(bookingTour);

        return "Booking canceled successfully. Refund amount: " + refundAmount;
    }

    private BigDecimal calculateRefund(BigDecimal depositAmount, long daysBeforeTour) {
        BigDecimal refundPercentage;

        if (daysBeforeTour >= 5) {
            refundPercentage = BigDecimal.valueOf(0.95);
        } else if (daysBeforeTour == 4) {
            refundPercentage = BigDecimal.valueOf(0.90);
        } else if (daysBeforeTour == 3) {
            refundPercentage = BigDecimal.valueOf(0.85);
        } else if (daysBeforeTour == 2) {
            refundPercentage = BigDecimal.valueOf(0.80);
        } else {
            refundPercentage = BigDecimal.valueOf(0.75);
        }

        return depositAmount.multiply(refundPercentage);
    }

    public List<TourPackage> findTop10ByOrderByTotalBookingsDesc() {
        List<TourPackage> top10Tours = bookingTourRepository.findAll().stream()
                .collect(Collectors.groupingBy(BookingTour::getTourpackage, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return top10Tours;
    }
    public CategoryTour createCategoryTour(CategoryTour categoryTour) {
        return categoryTourRepository.save(categoryTour);
    }
    
    public ThemeTour createThemeTour(ThemeTour themeTour) {
        return themeTourRepository.save(themeTour);
    }
    
    public SuitableTour createSuitableTour(SuitableTour suitableTour) {
        return suitableTourRepository.save(suitableTour);
    }

    public Optional<SuitableTour> findSuitableById(int id) {
        return suitableTourRepository.findById(id);
    }

    public SuitableTour updateSuitableTour(int id, String suitableTourName) {
        Optional<SuitableTour> suitableTourOptional = suitableTourRepository.findById(id);

        if (suitableTourOptional.isPresent()) {
            SuitableTour suitableTour = suitableTourOptional.get();
            suitableTour.setSuitableName(suitableTourName);
            return suitableTourRepository.save(suitableTour);
        }

        return null;
    }
    public Optional<CategoryTour> findCategoryById(int id) {
        return categoryTourRepository.findById(id);
    }
    public CategoryTour saveCategory(CategoryTour categoryTour) {
        return categoryTourRepository.save(categoryTour);
    }
    public Optional<ThemeTour> findThemeById(int id) {
        return themeTourRepository.findById(id);
    }

    public ThemeTour saveTheme(ThemeTour themeTour) {
        return themeTourRepository.save(themeTour);
    }
    public List<TourPackage> filterTourPackages(TourPackageFilterDTO filterDTO) {
        List<TourPackage> allTourPackages = tourPackageRepository.findAll();
        BigDecimal budget = filterDTO.getBudget() != null && !filterDTO.getBudget().isEmpty() 
            ? new BigDecimal(filterDTO.getBudget()) 
            : null;
        return allTourPackages.stream()
            .filter(pkg -> filterDTO.getCategoryTourId() == null || filterDTO.getCategoryTourId().isEmpty() ||
                pkg.getCategoryTours().stream().anyMatch(c -> filterDTO.getCategoryTourId().contains(c.getCategoryTourId())))
            .filter(pkg -> filterDTO.getThemeTourId() == null || filterDTO.getThemeTourId().isEmpty() ||
                pkg.getThemeTours().stream().anyMatch(t -> filterDTO.getThemeTourId().contains(t.getThemeTourId())))
            .filter(pkg -> filterDTO.getSuitableTourId() == null || filterDTO.getSuitableTourId().isEmpty() ||
                pkg.getSuitableTours().stream().anyMatch(s -> filterDTO.getSuitableTourId().contains(s.getSuitableTourId())))
            .filter(pkg -> budget == null || pkg.getPrice().compareTo(budget) <= 0)
            .collect(Collectors.toList());
    }
}
