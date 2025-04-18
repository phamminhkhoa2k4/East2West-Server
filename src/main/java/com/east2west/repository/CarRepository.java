package com.east2west.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


import com.east2west.models.Entity.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {

    List<Car> findByCarNameContainingIgnoreCase(String keyword);

    Page<Car> findAll(Pageable pageable);

    Optional<Car> findByCarName(String carName);

}