package com.east2west.models.DTO;

import com.east2west.models.enums.EActivity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityOrderDTO {

    private Long activityorderid;

    private EActivity activitytype;

    private Long refid;

    private Integer sortorder;

}
