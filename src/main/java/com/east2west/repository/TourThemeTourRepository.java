package com.east2west.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.east2west.models.Entity.TourThemeTour;

@Repository
public interface TourThemeTourRepository extends JpaRepository<TourThemeTour, Integer> {
     List<TourThemeTour> findByPackageid(int packageid);
}
