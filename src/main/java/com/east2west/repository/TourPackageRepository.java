package com.east2west.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.TourPackage;

@Repository
public interface TourPackageRepository extends JpaRepository<TourPackage, Integer> {


    List<TourPackage> findByTitleContainingIgnoreCase(String keyword);

    Optional<TourPackage> findByTitle(String title);

    Page<TourPackage> findAll(Pageable pageable);







    TourPackage findByPackageid(int packageid);
    List<TourPackage> findAll();



    @Query("SELECT tp FROM TourPackage tp JOIN tp.themes tt WHERE tt.themeName = :themeTourName")
    List<TourPackage> findByThemeTourName(@Param("themeTourName") String themeTourName);

    @Query("SELECT tp FROM TourPackage tp JOIN tp.suitable st WHERE st.suitableName = :suitableName")
    List<TourPackage> findBySuitableName(@Param("suitableName") String suitableName);

    boolean existsByTitle(String title);

    @Query("SELECT t FROM TourPackage t WHERE (:title IS NULL OR t.title LIKE %:title%) OR " +
            "(:minPrice IS NULL OR t.price >= :minPrice) OR " +
            "(:maxPrice IS NULL OR t.price <= :maxPrice) OR " +
            "(:categoryId IS NULL OR :categoryId IN (SELECT c.categoryId FROM t.categories c)) OR " +
            "(:themeId IS NULL OR :themeId IN (SELECT th.themeId FROM t.themes th)) OR " +
            "(:suitableId IS NULL OR :suitableId IN (SELECT s.suitableId FROM t.suitable s))")
    List<TourPackage> findByCriteria(@Param("title") String title,
                                     @Param("minPrice") Integer minPrice,
                                     @Param("maxPrice") Integer maxPrice,
                                     @Param("categoryId") Integer categoryId,
                                     @Param("themeId") Integer themeId,
                                     @Param("suitableId") Integer suitableId);


}