package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "departuredate")
public class DepartureDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "departuredateid")
    private int departuredateid;

    @Column(name = "departuredate")
    private Timestamp departuredate;

   
}