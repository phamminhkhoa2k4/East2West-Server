package com.east2west.models.payload.request;


import com.east2west.models.enums.EComparisonMethod;
import com.east2west.models.enums.EIdentityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentityUploadRequest {
    private String userId;

    private String identityForward;

    private String identityBackward;

    private  String nation;

    private EComparisonMethod comparisonMethod;

    private EIdentityType identityType;

}
