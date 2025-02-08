package com.east2west.models.Entity;

import jakarta.persistence.*;

// @Entity
// @Table(name = "hometypes")
public class HomeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hometypeid")
    private int hometypeid;

    @Column(name = "typename")
    private String typename;

}
