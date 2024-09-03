package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "homestayperks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HomestayPerk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestayperkid")
    private Integer homestayPerkID;

    @ManyToOne
    @JoinColumn(name = "homestayid", referencedColumnName = "homestayid")
    private Homestay homestay;

    @ManyToOne
    @JoinColumn(name = "perkid", referencedColumnName = "perkid")
    private Perk perk;
}

