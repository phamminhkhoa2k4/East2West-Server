package com.east2west.repository;

import com.east2west.models.Entity.Car;
import com.east2west.models.Entity.CarAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;

@Repository
public interface CarAvailabilityRepository extends JpaRepository<CarAvailability, Integer> {
    @Transactional
    void deleteAllByCarAndAvailabilityDateTimeAfter(Car car, Timestamp date);


}
