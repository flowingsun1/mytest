package com.transwarp.nxms.elasticsearch.domain.metrics.jvm;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 10:05
 */
@Getter
class Threads {
    private String count;
    @SerializedName("peak_count")
    private String peakCount;
}
