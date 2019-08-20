package com.transwarp.nxms.elasticsearch.domain.metrics.indices;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/15 17:17
 */
@Getter
public class TransLog {
    private String operations;
    @SerializedName("size_in_bytes")
    private String sizeInBytes;
}
