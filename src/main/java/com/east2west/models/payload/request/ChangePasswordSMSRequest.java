package com.east2west.models.payload.request;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordSMSRequest {
    private String phoneNumber;
}
