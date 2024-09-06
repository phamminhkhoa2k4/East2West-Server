package com.east2west.models.Entity;

import jakarta.persistence.*;


@Entity
@Table(name = "structures")
public class Structure {

    @Id
 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "structureid")
    private int structureid;

    public int getStructureid() {
        return this.structureid;
    }

    public void setStructureid(int structureid) {
        this.structureid = structureid;
    }


    @Column(name = "structurename")
    private String structurename;

    public String getStructurename() {
        return this.structurename;
    }

    public void setStructurename(String structurename) {
        this.structurename = structurename;
    }


}
