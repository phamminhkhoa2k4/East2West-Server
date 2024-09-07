package com.east2west.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.Structure;
@Repository
public interface StructureRepository extends JpaRepository<Structure,Integer>{

    
}
