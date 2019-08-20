package com.transwarp.nxms.elasticsearch.domain.metrics.jvm.mem;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 10:05
 */
@Getter
public class Mem {
    @SerializedName("heap_used_in_bytes")
    private String heapUsedInBytes;
    @SerializedName("heap_used_percent")
    private String heapUsedPercent;
    @SerializedName("heap_committed_in_bytes")
    private String heapCommittedInBytes;
    @SerializedName("heap_max_in_bytes")
    private String heapMaxInBytes;
    @SerializedName("non_heap_used_in_bytes")
    private String nonHeapUsedInBytes;
    @SerializedName("non_heap_committed_in_bytes")
    private String nonHeapCommittedInBytes;

    private Pools pools;
}
