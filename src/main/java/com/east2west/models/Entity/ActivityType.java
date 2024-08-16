package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "activitytype")
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activitytypeid")
    private int activitytypeid;

    public int getActivitytypeid() {
        return this.activitytypeid;
    }

    public void setActivitytypeid(int activitytypeid) {
        this.activitytypeid = activitytypeid;
    }

    @Column(name = "activitytypename")
    private String activitytypename;

    public String getActivitytypename() {
        return this.activitytypename;
    }

    public void setActivitytypename(String activitytypename) {
        this.activitytypename = activitytypename;
    }

}