package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "wards")
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wardid")
    private int wardid;

    @Column(name = "wardname")
    private String wardname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "districtid", referencedColumnName = "districtid")
    private District district;


}
