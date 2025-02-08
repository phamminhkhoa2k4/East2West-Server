package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "suitabletour")
public class SuitableTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suitabletourid")
    private int suitableTourId;

    @Column(name = "suitablename")
    private String suitableName;


}