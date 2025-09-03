package com.east2west.models.payload.request;

import lombok.*;

@Data
@Getter
@Setter
@Builder
public class VerifyCodeRequest {
    private String data;
    private String verificationCode;
}
