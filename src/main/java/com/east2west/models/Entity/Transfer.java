package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transferid")
    private int transferid;
    @Column(name = "transfername", length = 255)
    private String transfername;
    @Column(name = "transferthumbnail", columnDefinition = "TEXT")
    private String transferthumbnail;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "transferduration", length = 255)
    private String transferduration;


    
}