package com.east2west.models.Entity;


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
@Table(name = "car_availabilities")
public class CarAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_availability_id")
    private int carAvailabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carid", referencedColumnName = "carid")
    private Car car;

    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;

    @Column(name = "available_quantity")
    private int availableQuantity;

    @Column(name = "date")
    private Timestamp date;
}
