package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Models")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "modelid")
    private int modelid;

    @Column(name = "modelname")
    private String modelname;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "makeid", referencedColumnName = "makeid")
    private Make make;

}