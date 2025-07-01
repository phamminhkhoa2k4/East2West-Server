package com.east2west.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.Theme;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Integer> {
    List<Theme> findByThemeNameContainingIgnoreCase(String name);

    Optional<Theme> findByThemeName(String name);

    Set<Theme> findByThemeNameIn(List<String> name);

}