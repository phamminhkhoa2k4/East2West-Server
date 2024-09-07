package com.east2west.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.District;

public interface DistrictRepository extends JpaRepository<District,Integer> {

    
}
