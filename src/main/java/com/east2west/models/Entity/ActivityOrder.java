package com.east2west.models.Entity;


import com.east2west.models.enums.EActivity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "activityorder")
public class ActivityOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activityorderid")
    private Long activityorderid;


    @Enumerated(EnumType.STRING)
    @Column(name = "activitytype")
    private EActivity activitytype;

    @Column(name = "refid")
    private Long refid;


    @Column(name = "sortorder")
    private Integer sortorder;



    @ManyToOne
    @JoinColumn(name = "itineraryid")
    private Itinerary itinerary;


}
