package com.east2west.repository;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.TourCategoryTour;

@Repository
public interface TourCategoryTourRepository extends JpaRepository<TourCategoryTour, Integer> {
    List<TourCategoryTour> findByPackageid(int packageid);
}