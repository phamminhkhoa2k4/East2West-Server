package com.east2west.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.HomestayAvailability;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface HomestayAvailabilityRepository extends JpaRepository<HomestayAvailability,Integer>{


    @Modifying
    @Transactional
    @Query(value = "UPDATE homestayavailability SET pricepernight = :newPrice " +
            "WHERE pricepernight = (" +
            "    SELECT pricepernight " +
            "    FROM homestayavailability " +
            "    WHERE homestayid = :homestayId AND status = 'available' " +
            "    GROUP BY pricepernight " +
            "    ORDER BY COUNT(*) DESC, pricepernight " +
            "    LIMIT 1" +
            ") AND homestayid = :homestayId AND status = 'available'", nativeQuery = true)
    void updateBasePrice(@Param("homestayId") int homestayId, @Param("newPrice") BigDecimal newPrice);


    @Modifying
    @Transactional
    @Query(value = "UPDATE homestayavailability SET pricepernight = :newPrice WHERE homestayid = :homestayId AND EXTRACT(DOW FROM date) IN (0, 6) AND status = 'available'", nativeQuery = true)
    void updateWeekendPrice(@Param("homestayId") int homestayId, @Param("newPrice") BigDecimal newPrice);


    @Query("SELECT MIN(h.pricepernight) FROM HomestayAvailability h WHERE h.date = :date")
    BigDecimal findMinPriceByDate(@Param("date") Timestamp date);

    @Query("SELECT MAX(h.pricepernight) FROM HomestayAvailability h WHERE h.date = :date")
    BigDecimal findMaxPriceByDate(@Param("date") Timestamp date);

}
