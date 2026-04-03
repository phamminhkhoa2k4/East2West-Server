package com.east2west.repository;

import com.east2west.models.Entity.Homestay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


import com.east2west.models.Entity.Car;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    List<Car> findByCarNameContainingIgnoreCase(String keyword);

    Page<Car> findAll(Pageable pageable);

    Optional<Car> findByCarName(String carName);


    @Query(value = """
    WITH destination AS (
        SELECT ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326) AS geom
    )
    SELECT c.*
    FROM destination d
    INNER JOIN cars c 
        ON ST_DWithin(ST_Transform(c.geom, 4326), d.geom, :radius)
    INNER JOIN (
        SELECT ca.carid
        FROM car_availabilities ca
        WHERE ca.date BETWEEN :pickUpDate AND :dropOffDate
          AND ca.available_quantity > 0
        GROUP BY ca.carid
        HAVING COUNT(ca.date) = :days
    ) AS available_cars ON c.carid = available_cars.carid
    ORDER BY ST_Distance(ST_Transform(c.geom, 4326), d.geom)
""", nativeQuery = true)
    List<Car> searchCar(
            @Param("longitude") Double longitude,
            @Param("latitude") Double latitude,
            @Param("radius") Double radius,
            @Param("pickUpDate") LocalDate pickUpDate,
            @Param("dropOffDate") LocalDate dropOffDate,
            @Param("days") Integer days
    );


}