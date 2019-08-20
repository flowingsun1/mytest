package com.transwarp.nxms.elasticsearch.domain.metrics.indices;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/15 17:17
 */
@Getter
public class Warmer {
    private String current;
    private String total;
    @SerializedName("total_time_in_millis")
    private String totalTimeInMillis;
}
