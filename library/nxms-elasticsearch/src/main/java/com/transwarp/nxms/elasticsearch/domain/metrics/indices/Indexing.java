package com.transwarp.nxms.elasticsearch.domain.metrics.indices;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/15 17:17
 */
@Getter
public class Indexing {
    @SerializedName("index_total")
    private String indexTotal;

    @SerializedName("index_time_in_millis")
    private String indexTimeInMillis;

    @SerializedName("index_current")
    private String indexCurrent;

    @SerializedName("index_failed")
    private String indexFailed;

    @SerializedName("delete_total")
    private String deleteTotal;

    @SerializedName("delete_time_in_millis")
    private String deleteTimeInMillis;

    @SerializedName("delete_current")
    private String deleteCurrent;

    @SerializedName("noop_update_total")
    private String noopUpdateTotal;

    @SerializedName("is_throttled")
    private String isThrottled;

    @SerializedName("throttle_time_in_millis")
    private String throttleTimeInMillis;
}
