package com.east2west.models.payload.request;


import lombok.*;

@Data
@Getter
@Setter
@Builder
public class ResetPasswordRequest {
    private String email;
    private String verificationCode;
    private String newPassword;
}
