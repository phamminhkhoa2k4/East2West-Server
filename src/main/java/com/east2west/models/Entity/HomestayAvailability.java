package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.math.BigDecimal;
@Entity
@Table(name = "homestayavailability")
public class HomestayAvailability {

    // Getters and Setters
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestayavailabilityid")
    private int homestayavailabilityid;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homestayid", referencedColumnName = "homestayid")
    private Homestay homestay;

    @Getter
    @Setter
    @Column(name = "status")
    private String status;

    @Setter
    @Getter
    @Column(name = "pricepernight")
    private BigDecimal pricepernight;

    @Setter
    @Getter
    @Column(name = "date")
    private Timestamp date;

    public void setHomestayavailabilityid(int homestayavailabilityid) {
        this.homestayavailabilityid = homestayavailabilityid;
    }

}

   

