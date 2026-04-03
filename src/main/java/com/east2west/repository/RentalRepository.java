package com.east2west.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.east2west.models.Entity.Rental;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {


    List<Rental> findByCar_CaridAndUseridAndStatusIn(int carId,int userId, List<String> statuses);
    List<Rental> findByUserid(int userid);
}