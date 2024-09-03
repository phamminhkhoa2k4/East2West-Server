package com.east2west.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Structure;

public interface StructureRepository extends JpaRepository<Structure,Integer>{

    
}
