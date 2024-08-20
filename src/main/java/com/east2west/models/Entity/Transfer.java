package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transferid")
    private int transferid;

    public int getTransferid() {
        return this.transferid;
    }

    public void setTransferid(int transferid) {
        this.transferid = transferid;
    }

    @Column(name = "transfername", length = 255)
    private String transfername;

    public String getTransfername() {
        return this.transfername;
    }

    public void setTransfername(String transfername) {
        this.transfername = transfername;
    }

    @Column(name = "transferthumbnail", length = 255)
    private String transferthumbnail;

    public String getTransferthumbnail() {
        return this.transferthumbnail;
    }

    public void setTransferthumbnail(String transferthumbnail) {
        this.transferthumbnail = transferthumbnail;
    }

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "transferduration", length = 255)
    private String transferduration;

    public String getTransferduration() {
        return this.transferduration;
    }

    public void setTransferduration(String transferduration) {
        this.transferduration = transferduration;
    }

    
}