package com.east2west.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Accommodation;

public interface AccommodationRepository extends JpaRepository<Accommodation, Integer> {
    List<Accommodation> findByAccommodationnameContainingIgnoreCase(String name);

}
