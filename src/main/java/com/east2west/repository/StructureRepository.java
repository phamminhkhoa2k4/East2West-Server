package com.east2west.repository;


import com.east2west.models.Entity.Amenities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.Structure;

import java.util.List;

@Repository
public interface StructureRepository extends JpaRepository<Structure,Integer>{

    Page<Structure> findAll(Pageable pageable);

    List<Structure> findByStructurenameContainingIgnoreCase(String keyword);
}
