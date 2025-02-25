package com.east2west.models.payload.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class ChangePasswordRequest {
    private String token;
    private String newPassword;
}
