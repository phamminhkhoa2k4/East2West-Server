package com.east2west.models.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import jakarta.persistence.Column;


@Entity
@Table(name = "Makes")
public class Make {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "makeid")
    private int makeId;

    public int getMakeId() {
        return this.makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    @Column(name = "makename")
    private String makeName;

    public String getMakeName() {
        return this.makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    // Constructors, getters, setters, etc.
}