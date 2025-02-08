package com.east2west.models.Entity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "bookingtour")
public class BookingTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookingtourid")
    private int bookingtourid;
    @Column(name = "userid")
    private int userid;

    @ManyToOne
    @JoinColumn(name = "paymentid", referencedColumnName = "paymentid")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "packageid", referencedColumnName = "packageid")
    private TourPackage tourpackage;

    @Column(name = "bookingdate")
    private Timestamp bookingdate;

    @Column(name = "tourdate")
    private Timestamp tourdate;

    @Column(name = "numberofpeople")
    private int numberofpeople;

    @Column(name = "totalprice")
    private BigDecimal totalprice;

    @Column(name = "depositamount")
    private BigDecimal depositamount;

    @Column(name = "status")
    private String status;

    @Column(name = "refundamount")
    private BigDecimal refundamount;

    @Column(name = "refunddate")
    private Timestamp refunddate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "depositrefund")
    private boolean depositrefund;

}