package com.transwarp.nxms.elasticsearch.domain.metrics.other.fs;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 11:17
 */
@Getter
class IoTotal {
    private String operations;
    @SerializedName("read_operations")
    private String readOperations;
    @SerializedName("write_operations")
    private String writeOperations;
    @SerializedName("read_kilobytes")
    private String readKilobytes;
    @SerializedName("write_kilobytes")
    private String writeKilobytes;
}
