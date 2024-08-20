package com.east2west.repository;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.east2west.models.Entity.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {
   @Query("SELECT c FROM Car c LEFT JOIN FETCH c.model LEFT JOIN FETCH c.make LEFT JOIN FETCH c.type WHERE c.carId = :id")
    Optional<Car> findById(@Param("id") int id);
    //  @Query("SELECT r.car FROM Rental r WHERE r.user.id = :userId")
    // List<Car> findCarsByUserId(@Param("userId") int userId);
}