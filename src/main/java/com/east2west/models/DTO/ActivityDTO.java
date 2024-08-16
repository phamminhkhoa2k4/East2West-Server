package com.east2west.models.DTO;

import com.east2west.models.Entity.ActivityType;
public class ActivityDTO {
    private int activityid;
    private String activityname;
    private String description;
    private String timeperiod;
    private String thumbnail;
    private ActivityType activityType;

    public String getActivityname() {
        return this.activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeperiod() {
        return this.timeperiod;
    }

    public void setTimeperiod(String timeperiod) {
        this.timeperiod = timeperiod;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getActivityid() {
        return this.activityid;
    }

    public void setActivityid(int activityid) {
        this.activityid = activityid;
    }

    public ActivityType getActivityType() {
        return this.activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
    
}