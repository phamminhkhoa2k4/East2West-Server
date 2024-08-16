package com.east2west.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.TourCategoryTour;

@Repository
public interface TourCategoryTourRepository extends JpaRepository<TourCategoryTour, Integer> {
    List<TourCategoryTour> findByPackageid(int packageid);
}