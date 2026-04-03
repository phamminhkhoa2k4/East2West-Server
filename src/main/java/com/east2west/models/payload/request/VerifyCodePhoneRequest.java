package com.east2west.models.payload.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyCodePhoneRequest {
    private String userId;
    private String data;
    private String verificationCode;
}
