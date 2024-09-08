package com.east2west.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import com.east2west.exception.ResourceNotFoundException;
import com.east2west.models.DTO.BookingTourDTO;
import com.east2west.models.DTO.BookingTourFetch;
import com.east2west.models.DTO.CancelDTO;
import com.east2west.models.DTO.RefundFetch;
import com.east2west.models.DTO.TourPackageDTO;
import com.east2west.models.DTO.TourPackageDTO.DepartureDateDTO;
import com.east2west.models.DTO.TourPackageDetailDTO;
import com.east2west.models.DTO.TourPackageFilterDTO;
import com.east2west.models.DTO.UserFetch;
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
import java.time.format.DateTimeFormatterBuilder;
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

    public List<TourPackage> getAllTourpackages() {
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

    public List<DepartureDate> getAllDepartureDate() {
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

    public Optional<TourPackage> findById(int id) {
        return tourPackageRepository.findById(id);
    }

    public void save(TourPackage tourPackage) {
        tourPackageRepository.save(tourPackage);
    }

    public boolean existsByTitle(String title) {
        return tourPackageRepository.existsByTitle(title);
    }

    public TourPackage updateTourPackageFields(TourPackage tourPackage, TourPackageDTO tourPackageDTO) {
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
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
                .toFormatter();

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

        return tourPackage;
    }

    public TourPackage createTour(TourPackageDTO tourPackageDTO) {
        // Validate input fields
        if (tourPackageDTO.getTitle() == null || tourPackageDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (tourPackageDTO.getPrice() == null || tourPackageDTO.getPricereduce() == null) {
            throw new IllegalArgumentException("Price and reduced price cannot be null");
        }
        if (tourPackageDTO.getGroupsize() == null || tourPackageDTO.getDeposit() == null ||
                tourPackageDTO.getBookinghold() == null || tourPackageDTO.getBookingchange() == null) {
            throw new IllegalArgumentException("Groupsize, deposit, booking hold, and booking change cannot be null");
        }

        // Check for duplicate title
        if (tourPackageRepository.existsByTitle(tourPackageDTO.getTitle())) {
            throw new IllegalArgumentException("A tour package with this title already exists");
        }

        // Create a new TourPackage entity
        TourPackage tourPackage = new TourPackage();
        tourPackage.setTitle(tourPackageDTO.getTitle());
        tourPackage.setThumbnail(tourPackageDTO.getThumbnail());
        tourPackage.setPrice(tourPackageDTO.getPrice());
        tourPackage.setPricereduce(tourPackageDTO.getPricereduce());
        tourPackage.setGroupsize(tourPackageDTO.getGroupsize());
        tourPackage.setDeposit(tourPackageDTO.getDeposit());
        tourPackage.setBookinghold(tourPackageDTO.getBookinghold());
        tourPackage.setBookingchange(tourPackageDTO.getBookingchange());

        // Map CategoryTours
        List<CategoryTour> categoryTours = categoryTourRepository.findAllById(tourPackageDTO.getCategoryTourId());
        tourPackage.setCategoryTours(categoryTours);

        // Map ThemeTours
        List<ThemeTour> themeTours = themeTourRepository.findAllById(tourPackageDTO.getThemeTourId());
        tourPackage.setThemeTours(themeTours);

        // Map SuitableTours
        List<SuitableTour> suitableTours = suitableTourRepository.findAllById(tourPackageDTO.getSuitableTourId());
        tourPackage.setSuitableTours(suitableTours);

        // Map DepartureDates
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
                .toFormatter();
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

        // Save TourPackage entity
        return tourPackageRepository.save(tourPackage);
    }

    public TourPackage updateTour(TourPackageDTO tourPackageDTO) {
        Integer id = tourPackageDTO.getId();
        if (id == null || !tourPackageRepository.existsById(id)) {
            throw new ResourceNotFoundException("TourPackage not found with id " + id);
        }

        Optional<TourPackage> existingTourOpt = tourPackageRepository.findById(id);
        if (existingTourOpt.isPresent()) {
            TourPackage tourPackage = existingTourOpt.get();

            if (!tourPackage.getTitle().equals(tourPackageDTO.getTitle())) {
                // Check for duplicate title
                boolean exists = tourPackageRepository.existsByTitle(tourPackageDTO.getTitle());
                if (exists) {
                    throw new IllegalArgumentException("Title already exists");
                }
            }

            updateTourPackageFields(tourPackage, tourPackageDTO);
            return tourPackageRepository.save(tourPackage);
        } else {
            throw new ResourceNotFoundException("TourPackage not found with id " + id);
        }
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

    public BookingTour saveBookingTour(BookingTourDTO bookingTourDTO) {
        BookingTour bookingTour = new BookingTour();

        // Retrieve TourPackage
        TourPackage tourPackage = tourPackageRepository.findById(bookingTourDTO.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tour Package not found with id " + bookingTourDTO.getPackageId()));

        // Retrieve Payment
        Payment payment = paymentRepository.findById(bookingTourDTO.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id " + bookingTourDTO.getPaymentId()));

        // Set booking details
        bookingTour.setUserid(bookingTourDTO.getUserId());
        bookingTour.setTourpackage(tourPackage);
        bookingTour.setPayment(payment);
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        bookingTour.setBookingdate(timestamp);
        bookingTour.setTourdate(convertToTimestamp(bookingTourDTO.getTourDate()));
        bookingTour.setNumberofpeople(bookingTourDTO.getNumberOfPeople());
        bookingTour.setTotalprice(bookingTourDTO.getTotalPrice());
        bookingTour.setDepositamount(bookingTourDTO.getDepositAmount());
        bookingTour.setStatus("Waiting");
        bookingTour.setRefundamount(null);
        bookingTour.setRefunddate(null);
        bookingTour.setReason(null);
        bookingTour.setDepositrefund(false);

        // Save and return booking
        return bookingTourRepository.save(bookingTour);
    }

    public Timestamp convertToTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }
    public String cancelRefund(int id) {
        BookingTour bookingTour = bookingTourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "bookingTourId not found with id " + id));
        bookingTour.setRefundamount(null);
        bookingTour.setRefunddate(null);
        bookingTour.setStatus("Waiting");
        bookingTour.setReason("");
        bookingTour.setDepositrefund(false);
        bookingTourRepository.save(bookingTour);
        return "Cancel Refund ";
    }
    public String cancelBooking(CancelDTO cancelDTO) {
        BookingTour bookingTour = bookingTourRepository.findById(cancelDTO.getBookingTourId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "bookingTourId not found with id " + cancelDTO.getBookingTourId()));

        long daysBeforeTour = ChronoUnit.DAYS.between(LocalDate.now(),
                bookingTour.getTourdate().toLocalDateTime().toLocalDate());

        BigDecimal refundAmount = calculateRefund(bookingTour.getDepositamount(), daysBeforeTour);
        bookingTour.setRefundamount(refundAmount);
        bookingTour.setRefunddate(Timestamp.valueOf(LocalDateTime.now()));
        bookingTour.setStatus("Waiting Refund");
        bookingTour.setReason(cancelDTO.getReasson());
        bookingTour.setDepositrefund(true);

        bookingTourRepository.save(bookingTour);

        return "Booking canceled successfully. Refund amount: " + refundAmount;
    }
    public String cancelBookingEmployee(CancelDTO cancelDTO) {
        BookingTour bookingTour = bookingTourRepository.findById(cancelDTO.getBookingTourId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "bookingTourId not found with id " + cancelDTO.getBookingTourId()));

        long daysBeforeTour = ChronoUnit.DAYS.between(LocalDate.now(),
                bookingTour.getTourdate().toLocalDateTime().toLocalDate());

        BigDecimal refundAmount = calculateRefund(bookingTour.getDepositamount(), daysBeforeTour);
        bookingTour.setRefundamount(refundAmount);
        bookingTour.setRefunddate(Timestamp.valueOf(LocalDateTime.now()));
        bookingTour.setStatus("Refunded");
        bookingTour.setReason(cancelDTO.getReasson());
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

    public Optional<CategoryTour> findCategoryById(int id) {
        return categoryTourRepository.findById(id);
    }

    public Optional<ThemeTour> findThemeById(int id) {
        return themeTourRepository.findById(id);
    }

    public SuitableTour saveSuitableTour(SuitableTour suitableTour) {
        if (doesSuitableTourNameExist(suitableTour.getSuitableName(), suitableTour.getSuitableTourId())) {
            throw new IllegalArgumentException("SuitableTour name already exists.");
        }

        return suitableTourRepository.save(suitableTour);
    }

    private boolean doesSuitableTourNameExist(String name, int excludeId) {
        return suitableTourRepository.findBySuitableNameAndSuitableTourIdNot(name, excludeId).isPresent();
    }

    public CategoryTour saveCategory(CategoryTour categoryTour) {
        Optional<CategoryTour> existingCategory = categoryTourRepository.findById(categoryTour.getCategoryTourId());
        if (!existingCategory.isPresent()) {
            throw new IllegalArgumentException("CategoryTour not found for update.");
        }
        if (doesCategoryTourNameExist(categoryTour.getCategoryTourName(), categoryTour.getCategoryTourId())) {
            throw new IllegalArgumentException("CategoryTour name already exists.");
        }
        return categoryTourRepository.save(categoryTour);
    }

    private boolean doesCategoryTourNameExist(String name, int excludeId) {
        return categoryTourRepository.findByCategoryTourNameAndCategoryTourIdNot(name, excludeId).isPresent();
    }

    public ThemeTour saveTheme(ThemeTour themeTour) {
        Optional<ThemeTour> existingTheme = themeTourRepository.findById(themeTour.getThemeTourId());
        if (!existingTheme.isPresent()) {
            throw new IllegalArgumentException("ThemeTour not found for update.");
        }
        if (doesThemeTourNameExist(themeTour.getThemeTourName(), themeTour.getThemeTourId())) {
            throw new IllegalArgumentException("ThemeTour name already exists.");
        }
        return themeTourRepository.save(themeTour);
    }

    private boolean doesThemeTourNameExist(String name, int excludeId) {
        return themeTourRepository.findByThemeTourNameAndThemeTourIdNot(name, excludeId).isPresent();
    }

    public List<TourPackage> filterTourPackages(TourPackageFilterDTO filterDTO) {
        List<TourPackage> allTourPackages = tourPackageRepository.findAll();
        BigDecimal budget = filterDTO.getBudget() != null && !filterDTO.getBudget().isEmpty()
                ? new BigDecimal(filterDTO.getBudget())
                : null;
        return allTourPackages.stream()
                .filter(pkg -> filterDTO.getCategoryTourId() == null || filterDTO.getCategoryTourId().isEmpty() ||
                        pkg.getCategoryTours().stream()
                                .anyMatch(c -> filterDTO.getCategoryTourId().contains(c.getCategoryTourId())))
                .filter(pkg -> filterDTO.getThemeTourId() == null || filterDTO.getThemeTourId().isEmpty() ||
                        pkg.getThemeTours().stream()
                                .anyMatch(t -> filterDTO.getThemeTourId().contains(t.getThemeTourId())))
                .filter(pkg -> filterDTO.getSuitableTourId() == null || filterDTO.getSuitableTourId().isEmpty() ||
                        pkg.getSuitableTours().stream()
                                .anyMatch(s -> filterDTO.getSuitableTourId().contains(s.getSuitableTourId())))
                .filter(pkg -> budget == null || pkg.getPrice().compareTo(budget) <= 0)
                .collect(Collectors.toList());
    }

    public List<BookingTour> getListBookingByUser(int userId) {
        return bookingTourRepository.findByUserid(userId);
    }

    // public List<BookingTour> getBookingTour() {
    // return bookingTourRepository.findAll();
    // }
    public List<BookingTourFetch> getAllBookingTours() {
        List<BookingTour> bookings = bookingTourRepository.findAll();
        return bookings.stream()
        .filter(booking -> !booking.isDepositrefund())
        .map(booking -> {
            User user = userRepository.findById(booking.getUserid()).orElse(null);
            UserFetch userFetch = new UserFetch();

            userFetch.setFirstname(user.getFirstname());
            userFetch.setLastname(user.getLastname());
            userFetch.setPhone(user.getPhone());

            BookingTourFetch bookingTourFetch = new BookingTourFetch();
            bookingTourFetch.setBookingTourId(booking.getBookingtourid());
            bookingTourFetch.setTourTitle(booking.getTourpackage().getTitle()); // Tên tiêu đề tour
            bookingTourFetch.setUser(userFetch);
            bookingTourFetch.setStatus(booking.getStatus());
            bookingTourFetch.setTotalAmount(booking.getTotalprice());
            bookingTourFetch.setBookingDate(booking.getBookingdate());
            return bookingTourFetch;
        }).collect(Collectors.toList());
    }

    public List<RefundFetch> getAllRefunds() {
        List<BookingTour> bookings = bookingTourRepository.findAll();

        return bookings.stream()
                .filter(BookingTour::isDepositrefund) 
                .map(bookingTour -> {
                    User user = userRepository.findById(bookingTour.getUserid()).orElse(null);
                    UserFetch userFetch = new UserFetch();
                    if (user != null) {
                        userFetch.setFirstname(user.getFirstname());
                        userFetch.setLastname(user.getLastname());
                        userFetch.setPhone(user.getPhone());
                    }
                    RefundFetch refundFetch = new RefundFetch();
                    refundFetch.setBookingTourId(bookingTour.getBookingtourid());
                    refundFetch.setTourTitle(bookingTour.getTourpackage().getTitle()); 
                    refundFetch.setUser(userFetch);
                    refundFetch.setStatus(bookingTour.getStatus());
                    refundFetch.setReason(bookingTour.getReason()); 
                    refundFetch.setRefundAmount(bookingTour.getRefundamount());
                    refundFetch.setRefundDate(bookingTour.getRefunddate());
    
                    return refundFetch; // Return RefundFetch object
                })
                .collect(Collectors.toList());
    }
    

    public boolean deleteThemeTour(int id) {
        try {
            themeTourRepository.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException ex) {
            // Handle the exception and return false if there's a foreign key constraint
            // violation
            return false;
        }
    }

    public boolean deleteCategoryTour(int id) {
        try {
            categoryTourRepository.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException ex) {
            // Handle the exception and return false if there's a foreign key constraint
            // violation
            return false;
        }
    }

    public boolean deleteSuitable(int id) {
        try {
            suitableTourRepository.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException ex) {
            // Handle the exception and return false if there's a foreign key constraint
            // violation
            return false;
        }
    }

    public String eployeeSaveBookingTour(BookingTourDTO bookingTourDTO) {
        BookingTour bookingTour = new BookingTour();

        // Retrieve TourPackage
        TourPackage tourPackage = tourPackageRepository.findById(bookingTourDTO.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tour Package not found with id " + bookingTourDTO.getPackageId()));

        // Retrieve Payment
        Payment payment = paymentRepository.findById(bookingTourDTO.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id " + bookingTourDTO.getPaymentId()));

        // Set booking details
        bookingTour.setUserid(bookingTourDTO.getUserId());
        bookingTour.setTourpackage(tourPackage);
        bookingTour.setPayment(payment);
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        bookingTour.setBookingdate(timestamp);
       bookingTour.setTourdate(convertToTimestamp(bookingTourDTO.getTourDate()));
        bookingTour.setNumberofpeople(bookingTourDTO.getNumberOfPeople());
        bookingTour.setTotalprice(bookingTourDTO.getTotalPrice());
        bookingTour.setDepositamount(bookingTourDTO.getDepositAmount());
        bookingTour.setStatus("Waiting");
        bookingTour.setRefundamount(null);
        bookingTour.setRefunddate(null);
        bookingTour.setReason(null);
        bookingTour.setDepositrefund(false);
        return "Booking tour successfully";
    }

    public Optional<BookingTour> getBookingTourById(int bookingTourId) {
        return bookingTourRepository.findById(bookingTourId);
    }

    public BookingTour saveBookingTour(BookingTour bookingTour) {
        return bookingTourRepository.save(bookingTour);
    }

    public List<TourPackage> findByTitle(String title) {
        return tourPackageRepository.findByTitleContainingIgnoreCase(title);
    }




    public List<TourPackage> searchTourPackages(String title, Integer minPrice, Integer maxPrice, Integer categoryId, Integer themeId, Integer suitableId) {
        return tourPackageRepository.findByCriteria(title, minPrice, maxPrice, categoryId, themeId, suitableId);
    }
}
