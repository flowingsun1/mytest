package com.transwarp.nxms.elasticsearch.domain.metrics.other;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 11:11
 */
@Getter
class Script {
    private String compilations;
    @SerializedName("cache_evictions")
    private String cacheEvictions;
}
