package com.east2west.repository;

import com.east2west.models.Entity.Homestay;
import com.east2west.models.enums.EHomestayStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.HomestayAvailability;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HomestayAvailabilityRepository extends JpaRepository<HomestayAvailability,Integer>{


    @Modifying
    @Transactional
    @Query(value = "UPDATE homestayavailability SET pricepernight = :newPrice " +
            "WHERE pricepernight = (" +
            "    SELECT pricepernight " +
            "    FROM homestayavailability " +
            "    WHERE homestayid = :homestayId AND status = 'Available' " +
            "    GROUP BY pricepernight " +
            "    ORDER BY COUNT(*) DESC, pricepernight " +
            "    LIMIT 1" +
            ") AND homestayid = :homestayId AND status = 'Available'", nativeQuery = true)
    void updateBasePrice(@Param("homestayId") int homestayId, @Param("newPrice") BigDecimal newPrice);




    @Modifying
    @Transactional
    @Query(value = "UPDATE homestayavailability SET pricepernight = :newPrice " +
            "WHERE homestayid = :homestayId " +
            "AND EXTRACT(DOW FROM date) IN (0, 6) " +  // Sunday (0) and Saturday (6)
            "AND status = 'Available'", nativeQuery = true)
    void updateWeekendPrice(@Param("homestayId") int homestayId, @Param("newPrice") BigDecimal newPrice);




    @Query("SELECT MIN(h.pricepernight) FROM HomestayAvailability h WHERE CAST(h.date AS DATE) = :date")
    BigDecimal findMinPriceByDate(@Param("date") LocalDate date);

    @Query("SELECT MAX(h.pricepernight) FROM HomestayAvailability h WHERE CAST(h.date AS DATE) = :date")
    BigDecimal findMaxPriceByDate(@Param("date") LocalDate date);




    List<HomestayAvailability> findByHomestay_Homestayid(int homestayid);






}
