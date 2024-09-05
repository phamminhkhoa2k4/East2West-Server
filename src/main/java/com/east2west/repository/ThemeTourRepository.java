package com.east2west.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.ThemeTour;

@Repository
public interface ThemeTourRepository extends JpaRepository<ThemeTour, Integer> {
    List<ThemeTour> findByThemeTourNameIn(List<String> themeTourNames);
    Optional<ThemeTour> findByThemeTourNameAndThemeTourIdNot(String name, int excludeId);
}