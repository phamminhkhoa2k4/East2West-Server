package com.east2west.models.payload.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Builder
@Setter
public class JwtResponse {

    private String token;

    private int userId;

    private String username;

    private String email;

    private String password;

    private String lastname;

    private String firstname;

    private String phone;

    private String address;

    private List<String> roles;


}