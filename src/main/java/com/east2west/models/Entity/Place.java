package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placeid")
    private int placeid;

    @Column(name = "placename")
    private String placename;

    @Column(name = "placethumbnail")
    private String placethumbnail;

    @Column(name = "description")
    private String description;

    @Column(name = "placeduration")
    private String placeduration;

}
