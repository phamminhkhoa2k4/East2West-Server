package com.east2west.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.Suitable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SuitableRepository extends JpaRepository<Suitable, Integer> {
    List<Suitable> findBySuitableNameContainingIgnoreCase(String name);

    Optional<Suitable> findBySuitableName(String name);

    Set<Suitable> findBySuitableNameIn(List<String> name);
}