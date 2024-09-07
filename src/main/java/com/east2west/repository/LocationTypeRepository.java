package com.east2west.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.LocationType;
public interface LocationTypeRepository extends JpaRepository<LocationType,Integer>{

    Optional<LocationType> findByLocationtypenameAndLocationtypeidNot(String locationTypeName, int excludeLocationTypeId);
}
