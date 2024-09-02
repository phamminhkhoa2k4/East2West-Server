package com.east2west.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bouncycastle.util.Integers;
import org.hibernate.id.IdentifierGeneratorHelper.BigDecimalHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.east2west.models.DTO.RentalRevenueDTO;
import com.east2west.models.DTO.TourRevenueDTO;
import com.east2west.models.Entity.BookingTour;
import com.east2west.models.Entity.Rental;
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
        List<BookingTour> allBookings = bookingRepository.findAll();
    
        // Filter bookings by the specified date range
        List<BookingTour> filteredBookings = allBookings.stream()
                .filter(booking -> !booking.getBookingdate().toLocalDateTime().toLocalDate().isBefore(startDate) &&
                                   !booking.getBookingdate().toLocalDateTime().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());
    
        // Group bookings by tour and count the number of bookings per tour
        Map<Integer, Long> bookingCountMap = filteredBookings.stream()
        .collect(Collectors.groupingBy(
            booking -> booking.getTourpackage().getPackageid(), // Explicit cast to Long
            Collectors.counting()
        ));
    
        // Sort by the number of bookings and limit to top 10
        return bookingCountMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    int tourId = entry.getKey();
                    Long bookingCount = entry.getValue();
                    String tourTitle = getTourTitleById(tourId);
                    BigDecimal Revenue =getTourRevenue(tourId,bookingCount);
                    return new TourRevenueDTO(tourId, tourTitle, bookingCount,Revenue);
                })
                .collect(Collectors.toList());
    }

    // private TourRevenueDTO mapToTourRevenueDTO(Object[] result) {
    //     TourRevenueDTO dto = new TourRevenueDTO();
    //     dto.setTourId((Integer) result[0]);
    //     dto.setTourName((String) result[1]);
    //     dto.setTotalBookings((Long) result[2]);
    //     dto.setTotalRevenue((Double) result[3]);
    //     return dto;
    // }



    private BigDecimal getTourRevenue(int tourId ,Long bookingCount) {
        BigDecimal total = tourPackageRepository.findByPackageid(tourId).getPrice()
        .multiply(BigDecimal.valueOf(bookingCount));
        return total ; 
    }

    private String getTourTitleById(int tourId) {
        // This is a placeholder. Replace with actual logic to fetch the tour title.
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
            .filter(rental -> !rental.getRentalDate().toLocalDate().isBefore(startDate) &&
                              !rental.getRentalDate().toLocalDate().isAfter(endDate))
            .collect(Collectors.toList());

        // Group rentals by car and count the number of rentals per car
        Map<Integer, Long> rentalCountMap = filteredRentals.stream()
            .collect(Collectors.groupingBy(
                rental -> rental.getCar().getCarId(),
                Collectors.counting()
            ));

        // Sort by the number of rentals and limit to top 10
        return rentalCountMap.entrySet().stream()
            .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
            .limit(10)
            .map(entry -> {
                int carId = entry.getKey();
                Long rentalCount = entry.getValue();
                String carName = getCarNameById(carId);
                BigDecimal revenue = getCarRevenue(carId, rentalCount);
                return new RentalRevenueDTO(carId, carName, rentalCount, revenue);
            })
            .collect(Collectors.toList());
    }

    private BigDecimal getCarRevenue(int carId, Long rentalCount) {
        double pricePerDay = carRepository.findById(carId)
            .orElseThrow(() -> new RuntimeException("Car not found"))
            .getPricePerDay();
    
        return BigDecimal.valueOf(pricePerDay)
            .multiply(BigDecimal.valueOf(rentalCount));
    }

    private String getCarNameById(int carId) {
        return carRepository.findById(carId)
            .orElseThrow(() -> new RuntimeException("Car not found"))
            .getCarName();
    }
}