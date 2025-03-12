package com.east2west.models.Entity;
import jakarta.persistence.*;

import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Makes")
public class Make {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "makeid")
    private int makeid;

    @Column(name = "makename")
    private String makename;



}