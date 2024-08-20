package com.east2west.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Place;


public interface PlaceRepository extends JpaRepository<Place, Integer>{}
    

