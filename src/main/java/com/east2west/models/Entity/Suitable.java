package com.east2west.models.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "suitable")
public class Suitable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suitableid")
    private int suitableId;

    @Column(name = "suitablename")
    private String suitableName;

    @ManyToMany(mappedBy = "suitable")
    @JsonIgnore
    private Set<TourPackage> toursPackages = new HashSet<>();

}