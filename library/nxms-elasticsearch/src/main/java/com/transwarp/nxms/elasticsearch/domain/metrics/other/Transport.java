package com.transwarp.nxms.elasticsearch.domain.metrics.other;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 11:11
 */
@Getter
class Transport {
    @SerializedName("server_open")
    private String serverOpen;
    @SerializedName("rx_count")
    private String rxCount;
    @SerializedName("rx_size_in_bytes")
    private String rxSizeInBytes;
    @SerializedName("tx_count")
    private String txCount;
    @SerializedName("tx_size_in_bytes")
    private String txSizeInBytes;
}
