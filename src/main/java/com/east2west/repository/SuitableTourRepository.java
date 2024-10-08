package com.east2west.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.SuitableTour;

@Repository
public interface SuitableTourRepository extends JpaRepository<SuitableTour, Integer> {
    List<SuitableTour> findBySuitableNameIn(List<String> suitableNames);
    Optional<SuitableTour> findBySuitableNameAndSuitableTourIdNot(String name, int excludeId);
}