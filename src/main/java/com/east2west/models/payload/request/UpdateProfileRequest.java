package com.east2west.models.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;



@Getter
@Setter
@Builder
public class UpdateProfileRequest {
    @NotBlank(message = "Username is required.")
    private String username;
    @NotBlank(message = "First Name is required.")
    private String firstname;
    @NotBlank(message = "Last Name is required.")
    private String lastname;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    private String phone;
    @NotBlank(message = "Address is required.")
    private String address;

}
