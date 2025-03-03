package com.east2west.models.payload.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ForgotPasswordRequest {
    private String email;
}