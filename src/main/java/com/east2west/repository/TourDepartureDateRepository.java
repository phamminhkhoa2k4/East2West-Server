package com.east2west.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.TourDepartureDate;



@Repository
public interface TourDepartureDateRepository extends JpaRepository<TourDepartureDate, Integer> {
      List<TourDepartureDate> findByPackageid(int packageid);
}