package com.east2west.repository;
import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}