package com.east2west.models.payload.request;


import com.east2west.models.enums.EStatusVerify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivateHomestayRequest {

    private int homestayid;

    private int userId;

    private EStatusVerify status;
}
