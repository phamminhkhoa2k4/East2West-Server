package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hometypes")
public class HomeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hometypeid")
    private int hometypeid;

    @Column(name = "typename")
    private String typename;

    // Getters and Setters
    public int getHometypeid() {
        return hometypeid;
    }

    public void setHometypeid(int hometypeid) {
        this.hometypeid = hometypeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
