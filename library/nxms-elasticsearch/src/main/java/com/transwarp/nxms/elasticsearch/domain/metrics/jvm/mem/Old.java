package com.transwarp.nxms.elasticsearch.domain.metrics.jvm.mem;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 10:05
 */
@Getter
class Old {
    @SerializedName("used_in_bytes")
    private String usedInBytes;
    @SerializedName("max_in_bytes")
    private String maxInBytes;
    @SerializedName("peak_used_in_bytes")
    private String peakUsedInBytes;
    @SerializedName("peak_max_in_bytes")
    private String peakMaxInBytes;
}
