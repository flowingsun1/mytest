package com.transwarp.nxms.elasticsearch.domain.metrics.indices;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/15 17:17
 */
@Getter
public class QueryCache {
    @SerializedName("memory_size_in_bytes")
    private String memorySizeInBytes;
    @SerializedName("total_count")
    private String totalCount;
    @SerializedName("hit_count")
    private String hitCount;
    @SerializedName("miss_count")
    private String missCount;
    @SerializedName("cache_size")
    private String cacheSize;
    @SerializedName("cache_count")
    private String cacheCount;

    private String evictions;
}
