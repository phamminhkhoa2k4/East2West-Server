package com.east2west.models.DTO;

import com.east2west.models.enums.EDepartureDateStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartureDateDTO {

    private int departuredateid;

    private Timestamp departuredate;

    private EDepartureDateStatus status;

    private int availableseats;

    private BigDecimal priceoverride;
}
