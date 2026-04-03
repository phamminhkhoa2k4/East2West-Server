package com.east2west.repository;

import java.util.List;
import java.util.Optional;

import com.east2west.models.Entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Place;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer>{
    Optional<Place> findByPlacename(String placeName);

    boolean existsByPlacename(String placename);

    List<Place> findByPlacenameContainingIgnoreCase(String name);
}
    

