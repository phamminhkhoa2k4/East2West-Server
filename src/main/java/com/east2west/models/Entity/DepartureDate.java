package com.east2west.models.Entity;

import com.east2west.models.enums.EDepartureDateStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EDepartureDateStatus status;

    @Column(name = "availableseats")
    private int availableseats;

    @Column(name = "priceoveridde")
    private BigDecimal priceoverride;   
   
}