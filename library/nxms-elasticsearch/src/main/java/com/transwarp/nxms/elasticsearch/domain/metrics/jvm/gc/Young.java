package com.transwarp.nxms.elasticsearch.domain.metrics.jvm.gc;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 10:17
 */
@Getter
class Young {
    @SerializedName("collection_count")
    private String collectionCount;
    @SerializedName("collection_time_in_millis")
    private String collectionTimeInMillis;
}
