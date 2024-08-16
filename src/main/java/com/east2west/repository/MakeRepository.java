package com.east2west.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Make;


public interface MakeRepository extends JpaRepository<Make, Integer>{
    Optional<Make> findByMakeName(String makeName);
    
} 