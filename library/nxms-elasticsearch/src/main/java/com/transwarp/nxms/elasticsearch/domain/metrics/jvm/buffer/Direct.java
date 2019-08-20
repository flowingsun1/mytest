package com.transwarp.nxms.elasticsearch.domain.metrics.jvm.buffer;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 10:17
 */
@Getter
class Direct {
    private String count;
    @SerializedName("used_in_bytes")
    private String usedInBytes;
    @SerializedName("total_capacity_in_bytes")
    private String totalCapacityInBytes;
}
