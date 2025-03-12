package com.east2west.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.CityProvince;
import org.springframework.stereotype.Repository;

@Repository
public interface CityProvinceRepository extends JpaRepository<CityProvince, Integer>{
    
}
