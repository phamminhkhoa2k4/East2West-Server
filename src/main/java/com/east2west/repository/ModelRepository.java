package com.east2west.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.east2west.models.Entity.Model;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ModelRepository extends JpaRepository<Model, Integer>{
    Optional<Model> findByModelname(String modelName);

    Optional<Model> findByMake_MakeidAndModelname(int makeid, String modelname);

    Page<Model> findAll(Pageable pageable);

//    List<Model> findByModelnameContainingIgnoreCase(String keyword);

    @Query("SELECT m FROM Model m " +
            "JOIN m.make mk " +
            "WHERE LOWER(m.modelname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(mk.makename) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Model> searchByKeyword(@Param("keyword") String keyword);

}
