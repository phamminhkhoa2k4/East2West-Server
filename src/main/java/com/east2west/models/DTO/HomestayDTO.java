package com.east2west.models.DTO;

import java.math.BigDecimal;
import java.util.List;

public class HomestayDTO {
    private Integer homestayid;
    private int wardId;
    private int hometypeId;
    private Integer userid;
    private Double longitude;
    private Double latitude;
    private String title;
    private String address;
    private String geom;
    private String photos;
    private String description;
    private String exactinfo;
    private BigDecimal cleaningfee;
    private Boolean isapproved;
    private Integer maxguest;
    private List<Integer> perkIds;

    public Integer getHomestayid() {
        return this.homestayid;
    }

    public void setHomestayid(Integer homestayid) {
        this.homestayid = homestayid;
    }

    public int getWardId() {
        return this.wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }

    public int getHometypeId() {
        return this.hometypeId;
    }

    public void setHometypeId(int hometypeId) {
        this.hometypeId = hometypeId;
    }

    public Integer getUserid() {
        return this.userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGeom() {
        return this.geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getPhotos() {
        return this.photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExactinfo() {
        return this.exactinfo;
    }

    public void setExactinfo(String exactinfo) {
        this.exactinfo = exactinfo;
    }

    public BigDecimal getCleaningfee() {
        return this.cleaningfee;
    }

    public void setCleaningfee(BigDecimal cleaningfee) {
        this.cleaningfee = cleaningfee;
    }

    public Boolean getIsapproved() {
        return this.isapproved;
    }

    public void setIsapproved(Boolean isapproved) {
        this.isapproved = isapproved;
    }

    public Integer getMaxguest() {
        return this.maxguest;
    }

    public void setMaxguest(Integer maxguest) {
        this.maxguest = maxguest;
    }

    public List<Integer> getPerkIds() {
        return this.perkIds;
    }

    public void setPerkIds(List<Integer> perkIds) {
        this.perkIds = perkIds;
    }

    // Getters and Setters
}