package com.east2west.repository;
import java.util.List;
import java.util.Optional;

import com.east2west.models.Entity.Make;
import com.east2west.models.Entity.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.LocationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationTypeRepository extends JpaRepository<LocationType,Integer>{

    Optional<LocationType> findByLocationtypename(String makeName);

    Page<LocationType> findAll(Pageable pageable);

    @Query("SELECT l FROM LocationType l WHERE " +
            "LOWER(l.locationtypename) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(l.locationtypedescription) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<LocationType> searchByKeyword(@Param("keyword") String keyword);


}
