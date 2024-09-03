package com.east2west.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStatusDTO {
    private String status;
    private String message;
    private BookingHomestayDTO data;
}
