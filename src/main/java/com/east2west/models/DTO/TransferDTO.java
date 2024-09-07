package com.east2west.models.DTO;

public class TransferDTO {

    private int transferid;
    private String transfername;
    private String transferthumbnail;
    private String description;
    private String transferduration;

    public int getTransferid() {
        return this.transferid;
    }

    public void setTransferid(int transferid) {
        this.transferid = transferid;
    }

    public String getTransfername() {
        return this.transfername;
    }

    public void setTransfername(String transfername) {
        this.transfername = transfername;
    }

    public String getTransferthumbnail() {
        return this.transferthumbnail;
    }

    public void setTransferthumbnail(String transferthumbnail) {
        this.transferthumbnail = transferthumbnail;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransferduration() {
        return this.transferduration;
    }

    public void setTransferduration(String transferduration) {
        this.transferduration = transferduration;
    }
   
}

  