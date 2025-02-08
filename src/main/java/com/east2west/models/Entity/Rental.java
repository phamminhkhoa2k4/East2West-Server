package com.east2west.models.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;


import java.sql.Date;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rentalid")
    private int rentalid;

    //  @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "userid", referencedColumnName = "userid")
    // private User user;

    // public User getUser() {
    //     return this.user;
    // }

    // public void setUser(User user) {
    //     this.user = user;
    // }
    @Column(name = "userid")
    private int userid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carid", referencedColumnName = "carid")
    private Car car;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paymentid", referencedColumnName = "paymentid")
    private Payment payment;

    @Column(name = "rentaldate")
    private Date rentalDate;

    @Column(name = "returndate")
    private Date returnDate;

    @Column(name = "totalamount")
    private double totalAmount;

    @Column(name = "status")
    private String status;
   
}