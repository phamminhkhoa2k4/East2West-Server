package com.east2west.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.east2west.models.Entity.BookingTour;
import com.east2west.models.Entity.TourPackage;
import java.time.LocalDate;
@Repository
public interface BookingTourRepository extends JpaRepository<BookingTour, Integer>  {
    // @Query("SELECT b.tourPackage FROM Booking b WHERE b.bookingDate BETWEEN :start AND :end GROUP BY b.tourPackage ORDER BY COUNT(b) DESC")
    // List<TourPackage> findTopToursByBookingDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
