package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "structures")
public class Structure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "structureid")
    private Long structureid;

    public Long getStructureid() {
        return this.structureid;
    }

    public void setStructureid(Long structureid) {
        this.structureid = structureid;
    }

    @Column(name = "Structurename", nullable = false)
    private String structurename;

    public String getStructurename() {
        return this.structurename;
    }

    public void setStructurename(String structurename) {
        this.structurename = structurename;
    }

    // Constructors
    public Structure() {
    }

   
}