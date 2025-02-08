package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tourthemetour")
public class TourThemeTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tourthemetourid")
    private int tourThemeTourId;

    @Column(name = "packageid")
    private int packageid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "themetourid", referencedColumnName = "themetourid")
    private ThemeTour themeTour;

}