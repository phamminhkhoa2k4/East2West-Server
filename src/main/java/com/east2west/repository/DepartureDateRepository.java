package com.east2west.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.DepartureDate;

@Repository
public interface DepartureDateRepository extends JpaRepository<DepartureDate, Integer> {
    // List<DepartureDate> findByNameIn(List<String> names);
}