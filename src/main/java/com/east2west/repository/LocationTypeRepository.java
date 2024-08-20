package com.east2west.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.LocationType;
public interface LocationTypeRepository extends JpaRepository<LocationType,Integer>{

    
}
