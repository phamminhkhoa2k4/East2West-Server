package com.east2west.models.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "amenities")
public class Amenities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenitiesid")
    private int amenitiesid;

    @Column(name = "amenitiesname")
    private String amenitiesname;

    @Column(name = "amenitiesicon",columnDefinition = "Text")
    private String amenitiesicon;


    @ManyToMany(mappedBy = "amenities")
    @JsonIgnore
    private Set<Homestay> homestays = new HashSet<>();

}
