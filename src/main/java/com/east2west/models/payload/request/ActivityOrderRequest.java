package com.east2west.models.payload.request;

import com.east2west.models.enums.EActivity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityOrderRequest {

    @Enumerated(EnumType.STRING)
    private EActivity activitytype;

    private String activityname;

    private Integer sortorder;

}
