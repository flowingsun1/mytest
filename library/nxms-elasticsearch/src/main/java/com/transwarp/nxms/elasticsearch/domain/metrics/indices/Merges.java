package com.transwarp.nxms.elasticsearch.domain.metrics.indices;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/15 17:17
 */
@Getter
public class Merges {
    private String current;
    @SerializedName("current_docs")
    private String currentDocs;
    @SerializedName("current_size_in_bytes")
    private String currentSizeInBytes;
    private String total;
    @SerializedName("total_time_in_millis")
    private String totalTimeInMillis;
    @SerializedName("total_docs")
    private String totalDocs;
    @SerializedName("total_size_in_bytes")
    private String totalSizeInBytes;
    @SerializedName("total_stopped_time_in_millis")
    private String totalStoppedTimeInMillis;
    @SerializedName("total_throttled_time_in_millis")
    private String totalThrottledTimeInMillis;
    @SerializedName("total_auto_throttle_in_bytes")
    private String totalAutoThrottleInBytes;
}
