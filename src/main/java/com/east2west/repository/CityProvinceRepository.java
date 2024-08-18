package com.east2west.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.CityProvince;
public interface CityProvinceRepository extends JpaRepository<CityProvince, Integer>{
    
}
