package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import java.sql.Timestamp;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "homestayavailability")
public class HomestayAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestayavailabilityid")
    private int homestayavailabilityid;
   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homestayid", referencedColumnName = "homestayid")
    private Homestay homestay;
    
    @Column(name = "status")
    private String status;
   
    @Column(name = "pricepernight")
    private BigDecimal pricepernight;

    @Column(name = "date")
    private Timestamp date;
}

   

