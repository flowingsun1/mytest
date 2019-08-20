package com.transwarp.nxms.elasticsearch.domain.metrics.other.process;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 11:16
 */
@Getter
public class Process {
    @SerializedName("open_file_descriptors")
    private String openFileDescriptors;
    private Cpu cpu;
}
