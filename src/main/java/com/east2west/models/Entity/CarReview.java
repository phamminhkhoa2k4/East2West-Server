package com.east2west.models.Entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "carreviews")
public class CarReview {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carreviewid")
    private int carReviewId;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "carid", nullable = false)
    private Car car;

    @Column(name = "worththemoney")
    private Double worthTheMoney;

    @Column (name = "cleanliness")
    private Double cleanliness;

    @Column (name = "rentalcounter")
    private Double rentalCounter;

    @Column(name = "enthusiasticsupport")
    private Double enthusiasticSupport;

    @Column(name = "dropoffspeed")
    private Double dropOffSpeed;

    @Column(name  = "vehiclecondition")
    private Double vehicleCondition;

    @Column(name = "pickupspeed")
    private Double pickUpSpeed;

}
