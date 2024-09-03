package com.east2west.models.DTO;

import com.east2west.models.Entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TargetEmail {

//    private String avatar;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

//    private Role role;

    private  String phone;

    private  String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp checkin;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp checkout;

    private BigDecimal feeamount;

    private int numberofguest;

    private BigDecimal totalPrice;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
//    private Timestamp bookingdate;

    private String photos;

    private String title;
    private String location;

    private BigDecimal cleaningfee;

    private BigDecimal pricepernight;






}
