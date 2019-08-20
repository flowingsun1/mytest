package com.transwarp.nxms.elasticsearch.domain.metrics.other.fs;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 11:17
 */
@Getter
public class Fs {
    @SerializedName("io_stats")
    private IoStats ioStats;
    private Total total;
}
