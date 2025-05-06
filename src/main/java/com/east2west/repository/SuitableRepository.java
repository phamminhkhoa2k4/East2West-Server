package com.east2west.repository;

import com.east2west.models.Entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.Suitable;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuitableRepository extends JpaRepository<Suitable, Integer> {
    List<Suitable> findBySuitableNameContainingIgnoreCase(String name);

    Optional<Suitable> findBySuitableName(String name);
}