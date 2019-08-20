package com.transwarp.nxms.elasticsearch.domain.metrics.jvm;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 10:05
 */
@Getter
class Classes {
    @SerializedName("current_loaded_count")
    private String currentLoadedCount;
    @SerializedName("total_loaded_count")
    private String totalLoadedCount;
    @SerializedName("total_unloaded_count")
    private String totalUnloadedCount;
}
