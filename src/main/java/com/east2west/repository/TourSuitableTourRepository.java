package com.east2west.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.TourSuitableTour;
import com.east2west.models.Entity.TourThemeTour;

public interface TourSuitableTourRepository extends JpaRepository<TourSuitableTour, Integer> {
    List<TourSuitableTour> findByPackageid(int packageid);
}
