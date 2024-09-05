package com.east2west.repository;

import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.CategoryTour;

@Repository
public interface CategoryTourRepository extends JpaRepository<CategoryTour, Integer> {
    List<CategoryTour> findByCategoryTourNameIn(List<String> categoryTourNames);
    Optional<CategoryTour> findByCategoryTourNameAndCategoryTourIdNot(String name, int excludeId);
}