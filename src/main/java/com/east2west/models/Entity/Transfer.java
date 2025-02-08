package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transferid")
    private int transferid;

    @Column(name = "transfername")
    private String transfername;

    @Column(name = "transferthumbnail", columnDefinition = "TEXT")
    private String transferthumbnail;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "transferduration")
    private String transferduration;


    
}