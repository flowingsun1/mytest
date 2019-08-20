package com.transwarp.nxms.elasticsearch.domain.metrics.indices;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/15 17:17
 */
@Getter
public class Recovery {
    @SerializedName("current_as_source")
    private String currentAsSource;
    @SerializedName("current_as_target")
    private String currentAsTarget;
    @SerializedName("throttle_time_in_millis")
    private String throttleTimeInMillis;

}
