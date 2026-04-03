package com.east2west.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Amenities;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenitiesRepository extends JpaRepository<Amenities,Integer>{
    Page<Amenities> findAll(Pageable pageable);

    List<Amenities> findByAmenitiesnameContainingIgnoreCase(String keyword);

    List<Amenities> findByAmenitiesidIn(List<Integer> ids);

}
