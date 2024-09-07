package com.east2west.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.DepartureDate;

@Repository
public interface DepartureDateRepository extends JpaRepository<DepartureDate, Integer> {
    List<DepartureDate> findByDeparturedateIn(List<Timestamp> departuredates);
    @Query("SELECT d FROM DepartureDate d WHERE d.departuredate = :departuredate")
    Optional<DepartureDate> findByDeparturedate(@Param("departuredate") Timestamp departuredate);
}