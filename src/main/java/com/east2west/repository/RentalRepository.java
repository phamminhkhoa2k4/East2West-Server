package com.east2west.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.east2west.models.Entity.Rental;


public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByUserid(int userid);
}