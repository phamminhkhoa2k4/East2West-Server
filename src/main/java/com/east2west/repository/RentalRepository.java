package com.east2west.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.east2west.models.Entity.Rental;


public interface RentalRepository extends JpaRepository<Rental, Integer> {
    // Additional query methods can be defined here
}