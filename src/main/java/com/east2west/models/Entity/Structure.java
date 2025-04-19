package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "structures")
public class Structure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "structureid")
    private int structureid;

    @Column(name = "structurename")
    private String structurename;

    @Column(name = "structureicon")
    private String structureicon;
}
