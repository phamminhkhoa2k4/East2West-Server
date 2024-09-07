package com.east2west.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.Make;

@Repository
public interface MakeRepository extends JpaRepository<Make, Integer>{
    // Optional<Make> findByMakeName(String makeName);
    Optional<Make> findByMakeNameAndMakeIdNot(String makeName, int excludeMakeId);
} 