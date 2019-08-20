package com.transwarp.nxms.elasticsearch.domain.metrics.other.fs;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 11:17
 */
@Getter
class Total {
    @SerializedName("total_in_bytes")
    private String totalInBytes;
    @SerializedName("free_in_bytes")
    private String freeInBytes;
    @SerializedName("available_in_bytes")
    private String availableInBytes;
}
