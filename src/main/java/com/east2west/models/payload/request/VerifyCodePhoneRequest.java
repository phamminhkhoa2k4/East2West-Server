package com.east2west.models.payload.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class VerifyCodePhoneRequest {
    private String userId;
    private String data;
    private String verificationCode;
}
