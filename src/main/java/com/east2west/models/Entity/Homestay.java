package com.east2west.models.Entity;

import com.east2west.util.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Entity
@Table(name = "homestays")
public class Homestay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestayid")
    private int homestayid;

    public int getHomestayid() {
        return this.homestayid;
    }

    public void setHomestayid(int homestayid) {
        this.homestayid = homestayid;
    }


    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wardid", referencedColumnName = "wardid")
    private Ward ward;

    public Ward getWard() {
        return this.ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    // alter small
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "structureid", referencedColumnName = "structureid")
    private Structure structure;

    public Structure getStructure() {
        return this.structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    
    @Column(name = "type")
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }


    
    @Column(name = "userid")
    private int userid;

    public int getUserid() {
        return this.userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    
    @Column(name = "longitude")
    private double longitude;

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

   
    @Column(name = "latitude")
    private double latitude;

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    
    @Column(name = "title")
    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    
    @Column(name = "address")
    private String address;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
    @Column(name = "geom")
    private String geom;

    public String getGeom() {
        return this.geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }



   
    @Column(name = "photos")
    @Convert(converter = StringListConverter.class)
    private List<String> photos;

    public List<String> getPhotos() {
        return this.photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    
    @Column(name = "description")
    private String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

   
    @Column(name = "extrainfo")
    private String extrainfo;

    public String getExtrainfo() {
        return this.extrainfo;
    }

    public void setExtrainfo(String extrainfo) {
        this.extrainfo = extrainfo;
    }

    
    @Column(name = "cleaningfee")
    private BigDecimal cleaningfee;

    public BigDecimal getCleaningfee() {
        return this.cleaningfee;
    }

    public void setCleaningfee(BigDecimal cleaningfee) {
        this.cleaningfee = cleaningfee;
    }

    
    @Column(name = "isapproved")
    private boolean isapproved;

    public boolean isIsapproved() {
        return this.isapproved;
    }

    public void setIsapproved(boolean isapproved) {
        this.isapproved = isapproved;
    }

    
    @Column(name = "maxguest")
    private int maxguest;

    public int getMaxguest() {
        return this.maxguest;
    }

    public void setMaxguest(int maxguest) {
        this.maxguest = maxguest;
    }

    // Getters and Setters
    
    @ManyToMany
    @JoinTable(
        name = "homestayperks",
        joinColumns = @JoinColumn(name = "homestayid"),
        inverseJoinColumns = @JoinColumn(name = "perkid")
    )
    private List<Perk> perks;

    public List<Perk> getPerks() {
        return this.perks;
    }

    public void setPerks(List<Perk> perks) {
        this.perks = perks;
    }

    
    @OneToMany(mappedBy = "homestay", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HomestayAvailability> homestayAvailabilityList;

    public List<HomestayAvailability> getHomestayAvailabilityList() {
        return this.homestayAvailabilityList;
    }

    public void setHomestayAvailabilityList(List<HomestayAvailability> homestayAvailabilityList) {
        this.homestayAvailabilityList = homestayAvailabilityList;
    }

}
