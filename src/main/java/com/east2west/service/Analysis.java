package com.east2west.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.PropertySource.Comparator;
import org.bouncycastle.util.Integers;
import org.hibernate.id.IdentifierGeneratorHelper.BigDecimalHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.east2west.models.DTO.RentalRevenueDTO;
import com.east2west.models.DTO.TourRevenueDTO;
import com.east2west.models.Entity.BookingTour;
import com.east2west.models.Entity.Rental;
import com.east2west.models.Entity.TourPackage;
import com.east2west.repository.BookingTourRepository;
import com.east2west.repository.CarRepository;
import com.east2west.repository.RentalRepository;
import com.east2west.repository.TourPackageRepository;
import java.math.BigDecimal;

@Service
public class Analysis {
    @Autowired
    private BookingTourRepository bookingRepository;
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private TourPackageRepository tourPackageRepository;

    public List<TourRevenueDTO> getTopBookedToursByMonth(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        return getTopBookedTours(startDate, endDate);
    }

    public List<TourRevenueDTO> getTopBookedToursByWeek(int year, int week) {
        LocalDate startDate = LocalDate.ofYearDay(year, (week - 1) * 7 + 1);
        LocalDate endDate = startDate.plusDays(6);
        return getTopBookedTours(startDate, endDate);
    }

    public List<TourRevenueDTO> getTopBookedToursByYear(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return getTopBookedTours(startDate, endDate);
    }

    private List<TourRevenueDTO> getTopBookedTours(LocalDate startDate, LocalDate endDate) {
        return findTopBookedTours(startDate, endDate);
    }

    private List<TourRevenueDTO> findTopBookedTours(LocalDate startDate, LocalDate endDate) {
        // Fetch all bookings
        List<BookingTour> allBookings = bookingRepository.findAll();
    
        // Filter bookings by date range and statuses "Confirmed" or "Refunded"
        List<BookingTour> filteredBookings = allBookings.stream()
                .filter(booking -> ("Confirmed".equals(booking.getStatus()) || "Refunded".equals(booking.getStatus())) &&
                        !booking.getBookingdate().toLocalDateTime().toLocalDate().isBefore(startDate) &&
                        !booking.getBookingdate().toLocalDateTime().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());
    
        Map<Integer, TourSummary> bookingSummaryMap = filteredBookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getTourpackage().getPackageid(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                bookings -> new TourSummary(
                                        bookings.size(), // Total count of bookings for this tour
                                        bookings.stream()
                                                .map(booking -> booking.getTotalprice() != null
                                                        ? booking.getTotalprice()
                                                        : BigDecimal.ZERO)
                                                .reduce(BigDecimal.ZERO, BigDecimal::add), // Total revenue before refunds
                                        bookings.stream()
                                                .map(booking -> booking.getRefundamount() != null
                                                        ? booking.getRefundamount()
                                                        : BigDecimal.ZERO)
                                                .reduce(BigDecimal.ZERO, BigDecimal::add) // Total refund
                                )
                        )
                ));
    
        // Sort by booking count in descending order and limit to top 10
        return bookingSummaryMap.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue().getBookingCount(),
                        entry1.getValue().getBookingCount()))
                .limit(10)
                .map(entry -> {
                    int tourId = entry.getKey();
                    TourSummary summary = entry.getValue();
                    
                    // Fetch the TourPackage and handle the null case
                    TourPackage tourPackage = tourPackageRepository.findByPackageid(tourId);
                    String tourTitle = (tourPackage != null) ? tourPackage.getTitle() : "Unknown Title";
    
                    // Calculate final revenue by subtracting total refunds from total revenue
                    BigDecimal finalRevenue = summary.getTotalRevenue().subtract(summary.getTotalRefund());
    
                    return new TourRevenueDTO(tourId, tourTitle, summary.getBookingCount(), finalRevenue, summary.getTotalRefund());
                })
                .collect(Collectors.toList());
    }
    


    // Helper class to store booking count, total revenue, and total refund for each
    // tour
    private static class TourSummary {
        private final long bookingCount;
        private final BigDecimal totalRevenue;
        private final BigDecimal totalRefund;

        public TourSummary(long bookingCount, BigDecimal totalRevenue, BigDecimal totalRefund) {
            this.bookingCount = bookingCount;
            this.totalRevenue = totalRevenue;
            this.totalRefund = totalRefund;
        }

        public long getBookingCount() {
            return bookingCount;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        public BigDecimal getTotalRefund() {
            return totalRefund;
        }
    }

    private String getTourTitleById(int tourId) {
        // Hàm này lấy tiêu đề của tour theo ID
        return tourPackageRepository.findByPackageid(tourId).getTitle();
    }

    public List<RentalRevenueDTO> getTopRentedCarsByMonth(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        return getTopRentedCars(startDate, endDate);
    }

    public List<RentalRevenueDTO> getTopRentedCarsByWeek(int year, int week) {
        LocalDate startDate = LocalDate.ofYearDay(year, (week - 1) * 7 + 1);
        LocalDate endDate = startDate.plusDays(6);
        return getTopRentedCars(startDate, endDate);
    }

    public List<RentalRevenueDTO> getTopRentedCarsByYear(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return getTopRentedCars(startDate, endDate);
    }

    private List<RentalRevenueDTO> getTopRentedCars(LocalDate startDate, LocalDate endDate) {
        List<Rental> allRentals = rentalRepository.findAll();

        // Filter rentals by the specified date range
        List<Rental> filteredRentals = allRentals.stream()
                .filter(rental -> {
                    LocalDate rentalDate = rental.getRentalDate().toLocalDate();
                    return !rentalDate.isBefore(startDate) && !rentalDate.isAfter(endDate);
                })
                .collect(Collectors.toList());

        // Group rentals by car and calculate the total revenue and rental count for
        // each car
        Map<Integer, RentalSummary> rentalSummaryMap = filteredRentals.stream()
                .collect(Collectors.groupingBy(
                        rental -> rental.getCar().getCarId(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                rentalsList -> new RentalSummary(
                                        (long) rentalsList.size(), // Cast to long
                                        rentalsList.stream()
                                                .map(rental -> {
                                                    // Adjust based on the return type of getTotalAmount()
                                                    if (rental.getTotalAmount() != 0) {
                                                        return BigDecimal.valueOf(rental.getTotalAmount());
                                                    } else {
                                                        return BigDecimal.ZERO;
                                                    }
                                                })
                                                .reduce(BigDecimal.ZERO, BigDecimal::add)))));

        // Sort by the number of rentals in descending order and limit to top 10
        return rentalSummaryMap.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(
                        entry2.getValue().getRentalCount(),
                        entry1.getValue().getRentalCount()))
                .limit(10)
                .map(entry -> {
                    int carId = entry.getKey();
                    RentalSummary summary = entry.getValue();
                    String carName = getCarNameById(carId);
                    return new RentalRevenueDTO(
                            carId,
                            carName,
                            summary.getRentalCount(),
                            summary.getTotalRevenue());
                })
                .collect(Collectors.toList());
    }

    // Helper class to store total revenue and rental count for each car
    private static class RentalSummary {
        private long rentalCount;
        private BigDecimal totalRevenue;

        public RentalSummary(long rentalCount, BigDecimal totalRevenue) {
            this.rentalCount = rentalCount;
            this.totalRevenue = totalRevenue;
        }

        public long getRentalCount() {
            return rentalCount;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        // Optional: Setters can be removed if not used elsewhere
        public void setRentalCount(long rentalCount) {
            this.rentalCount = rentalCount;
        }

        public void setTotalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
        }
    }

    private String getCarNameById(int carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"))
                .getCarName();
    }

}