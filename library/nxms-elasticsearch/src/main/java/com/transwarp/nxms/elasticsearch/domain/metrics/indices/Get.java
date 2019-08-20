package com.transwarp.nxms.elasticsearch.domain.metrics.indices;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/15 17:17
 */
@Getter
public class Get {
    private String total;

    @SerializedName("time_in_millis")
    private String timeInMillis;

    @SerializedName("exists_total")
    private String existsTotal;

    @SerializedName("exists_time_in_millis")
    private String existsTimeInMillis;

    @SerializedName("missing_total")
    private String missingTotal;

    @SerializedName("missing_time_in_millis")
    private String missingTimeInMillis;

    private String current;
}
