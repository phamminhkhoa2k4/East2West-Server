package com.east2west.models.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activityid")
    private int activityid;

    public int getActivityid() {
        return this.activityid;
    }

    public void setActivityid(int activityid) {
        this.activityid = activityid;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itineraryid", referencedColumnName = "itineraryid")
    @JsonIgnore
    private Itinerary itinerary;

    public Itinerary getItinerary() {
        return this.itinerary;
    }

    public void setItinerary(Itinerary itineraryid) {
        this.itinerary = itineraryid;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activitytypeid", referencedColumnName = "activitytypeid")
    private ActivityType activitytype;

    public ActivityType getActivitytype() {
        return this.activitytype;
    }

    public void setActivitytype(ActivityType activitytypeid) {
        this.activitytype = activitytypeid;
    }

    @Column(name = "activityname")
    private String activityname;

    public String getActivityname() {
        return this.activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    @Column(name = "description")
    private String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "timeperiod")
    private String timeperiod;

    public String getTimeperiod() {
        return this.timeperiod;
    }

    public void setTimeperiod(String timeperiod) {
        this.timeperiod = timeperiod;
    }

    @Column(name = "thumbnail")
    private String thumbnail;

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
