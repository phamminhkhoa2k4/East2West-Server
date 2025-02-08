package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "toursuitabletour")
public class TourSuitableTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "toursuitabletourid")
    private int toursuitabletourid;

    @Column(name = "packageid")
    private int packageid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "suitabletourid", referencedColumnName = "suitabletourid")
    private SuitableTour suitabletourid;


}