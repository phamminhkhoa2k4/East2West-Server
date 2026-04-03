package com.east2west.models.payload.response;


import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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