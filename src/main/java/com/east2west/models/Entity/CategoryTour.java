package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "categorytour")
public class CategoryTour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categorytourid")
    private int categoryTourId;

    @Column(name = "categorytourname")
    private String categoryTourName;

}