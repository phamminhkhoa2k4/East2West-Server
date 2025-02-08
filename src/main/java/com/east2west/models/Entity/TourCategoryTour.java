package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tourcategorytour")
public class TourCategoryTour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tourcategorytourid")
    private int tourCategoryTourId;
    
    @Column(name = "packageid")
    private int packageid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorytourid", referencedColumnName = "categorytourid")
    private CategoryTour categoryTour;

}