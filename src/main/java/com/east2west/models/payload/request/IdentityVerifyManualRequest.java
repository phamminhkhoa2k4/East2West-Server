package com.east2west.models.payload.request;


import com.east2west.models.enums.EComparisonMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentityVerifyManualRequest {
    private String userId;
    private String selfie;
    private EComparisonMethod comparisonMethod;
}
