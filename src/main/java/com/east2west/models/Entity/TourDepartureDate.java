package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tourdeparturedate")
public class TourDepartureDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tourdeparturedateid")
    private int tourdeparturedateid;

    public int getTourdeparturedateid() {
        return this.tourdeparturedateid;
    }

    public void setTourdeparturedateid(int tourdeparturedateid) {
        this.tourdeparturedateid = tourdeparturedateid;
    }

    @Column(name = "packageid")
    private int packageid;

    public int getPackageid() {
        return this.packageid;
    }

    public void setPackageid(int packageid) {
        this.packageid = packageid;
    }

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "departuredateid", referencedColumnName = "departuredateid")
private DepartureDate departureDate;



public DepartureDate getDepartureDate() {
    return departureDate;
}

public void setDepartureDate(DepartureDate departureDate) {
    this.departureDate = departureDate;
}

}