package com.east2west.repository;
import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.east2west.models.DTO.CarDTO;
import com.east2west.models.Entity.Car;
import com.east2west.models.Entity.TourPackage;

public interface CarRepository extends JpaRepository<Car, Integer> {
   @Query("SELECT c FROM Car c LEFT JOIN FETCH c.model LEFT JOIN FETCH c.make LEFT JOIN FETCH c.type WHERE c.carId = :id")
    Optional<Car> findById(@Param("id") int id);
    //  @Query("SELECT r.car FROM Rental r WHERE r.user.id = :userId")
    // List<Car> findCarsByUserId(@Param("userId") int userId);
    boolean existsByCarName(String carName);

    @Query("SELECT t FROM Car t WHERE LOWER(t.carName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Car> findByTitleContainingIgnoreCase(@Param("name") String name);
    
  
    Optional<Car> findByCarNameAndCarIdNot(String carName, int carId);
    @Query("SELECT c FROM Car c " +
            "WHERE (:carName IS NULL OR c.carName LIKE %:carName%) " +
            "AND (:modelName IS NULL OR c.model.modelName = :modelName) " +
            "AND (:makeName IS NULL OR c.make.makeName = :makeName) " +
            "AND (:typeName IS NULL OR c.type.typeName = :typeName) " +
            "AND (:airConditioned IS NULL OR c.airConditioned = :airConditioned) " +
            "AND (:minPrice IS NULL OR c.pricePerDay >= :minPrice) " +
            "AND (:maxPrice IS NULL OR c.pricePerDay <= :maxPrice) " +
            "AND (:location IS NULL OR c.location = :location) " +
            "AND (:minMiles IS NULL OR c.miles >= :minMiles) " +
            "AND (:maxMiles IS NULL OR c.miles <= :maxMiles)")
    List<Car> findByFilters(
            @Param("carName") String carName,
            @Param("modelName") String modelName,
            @Param("makeName") String makeName,
            @Param("typeName") String typeName,
            @Param("airConditioned") Boolean airConditioned,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("location") String location,
            @Param("minMiles") Long minMiles,
            @Param("maxMiles") Long maxMiles);

}