package com.east2west.models.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class UserInfoResponse {
    private int userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private List<String> roles;


    public UserInfoResponse(int userId, String username, String firstname, String lastname, 
                            String email, String phone, String address, List<String> roles) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.roles = roles;
    }

}
