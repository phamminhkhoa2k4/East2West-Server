package com.east2west.repository;

import java.util.List;
import java.util.Optional;

import com.east2west.models.Entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Accommodation;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Integer> {
    boolean existsByAccommodationname(String accommodationname);
    Optional<Accommodation> findByAccommodationname(String name);

    List<Accommodation> findByAccommodationnameContainingIgnoreCase(String name);

}
