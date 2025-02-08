package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tourdeparturedate")
public class TourDepartureDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tourdeparturedateid")
    private int tourdeparturedateid;

    @Column(name = "packageid")
    private int packageid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departuredateid", referencedColumnName = "departuredateid")
    private DepartureDate departureDate;

}