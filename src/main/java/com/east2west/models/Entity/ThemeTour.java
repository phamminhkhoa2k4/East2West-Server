package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "themetour")
public class ThemeTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "themetourid")
    private int themeTourId;

    @Column(name = "themetourname")
    private String themeTourName;

}