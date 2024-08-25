package com.east2west.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.TourPackage;

@Repository
public interface TourPackageRepository extends JpaRepository<TourPackage, Integer> {
    TourPackage findByPackageid(int packageid);

    @Query("SELECT tp FROM TourPackage tp JOIN tp.categoryTours ct WHERE ct.categoryTourName = :categoryTourName")
List<TourPackage> findByCategoryTourName(@Param("categoryTourName") String categoryTourName);


@Query("SELECT tp FROM TourPackage tp JOIN tp.themeTours tt WHERE tt.themeTourName = :themeTourName")
List<TourPackage> findByThemeTourName(@Param("themeTourName") String themeTourName);

@Query("SELECT tp FROM TourPackage tp JOIN tp.suitableTours st WHERE st.suitableName = :suitableName")
List<TourPackage> findBySuitableName(@Param("suitableName") String suitableName);



}