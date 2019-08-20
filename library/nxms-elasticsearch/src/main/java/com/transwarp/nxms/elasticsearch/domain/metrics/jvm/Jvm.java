package com.transwarp.nxms.elasticsearch.domain.metrics.jvm;


import com.google.gson.annotations.SerializedName;
import com.transwarp.nxms.elasticsearch.domain.metrics.jvm.buffer.BufferPools;
import com.transwarp.nxms.elasticsearch.domain.metrics.jvm.gc.Gc;
import com.transwarp.nxms.elasticsearch.domain.metrics.jvm.mem.Mem;
import lombok.Getter;


/**
 * @Author: lyy
 * @Date: 2019/8/9 16:50
 */
@Getter
public class Jvm {
    private Mem mem;
    private Threads threads;
    private Gc gc;
    @SerializedName("buffer_pools")
    private BufferPools bufferPools;
    private Classes classes;

}
