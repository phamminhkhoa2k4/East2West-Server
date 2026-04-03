package com.east2west.models.payload.request;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {
    private String email;
    private String verificationCode;
    private String newPassword;
}
