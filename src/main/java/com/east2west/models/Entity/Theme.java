package com.east2west.models.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "themeid")
    private int themeId;

    @Column(name = "themename")
    private String themeName;

    @ManyToMany(mappedBy = "themes")
    @JsonIgnore
    private Set<TourPackage> toursPackages = new HashSet<>();

}