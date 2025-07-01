package com.east2west.models.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResDTO {
    private String status;
    private String message;
    private String URL;
}
