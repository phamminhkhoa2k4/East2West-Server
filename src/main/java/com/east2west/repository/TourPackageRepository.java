package com.east2west.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.east2west.models.DTO.TourPackageDTO;
import com.east2west.models.Entity.TourPackage;

@Repository
public interface TourPackageRepository extends JpaRepository<TourPackage, Integer> {
    TourPackage findByPackageid(int packageid);
    List<TourPackage> findAll();

    @Query("SELECT tp FROM TourPackage tp JOIN tp.categoryTours ct WHERE ct.categoryTourName = :categoryTourName")
    List<TourPackage> findByCategoryTourName(@Param("categoryTourName") String categoryTourName);

    @Query("SELECT tp FROM TourPackage tp JOIN tp.themeTours tt WHERE tt.themeTourName = :themeTourName")
    List<TourPackage> findByThemeTourName(@Param("themeTourName") String themeTourName);

    @Query("SELECT tp FROM TourPackage tp JOIN tp.suitableTours st WHERE st.suitableName = :suitableName")
    List<TourPackage> findBySuitableName(@Param("suitableName") String suitableName);

    boolean existsByTitle(String title);

    @Query("SELECT t FROM TourPackage t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<TourPackage> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Query("SELECT t FROM TourPackage t WHERE (:title IS NULL OR t.title LIKE %:title%) AND " +
            "(:minPrice IS NULL OR t.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR t.price <= :maxPrice) AND " +
            "(:categoryId IS NULL OR :categoryId IN (SELECT c.categoryTourId FROM t.categoryTours c)) AND " +
            "(:themeId IS NULL OR :themeId IN (SELECT th.themeTourId FROM t.themeTours th)) AND " +
            "(:suitableId IS NULL OR :suitableId IN (SELECT s.suitableTourId FROM t.suitableTours s))")
    List<TourPackage> findByCriteria(@Param("title") String title,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("categoryId") Integer categoryId,
            @Param("themeId") Integer themeId,
            @Param("suitableId") Integer suitableId);

}