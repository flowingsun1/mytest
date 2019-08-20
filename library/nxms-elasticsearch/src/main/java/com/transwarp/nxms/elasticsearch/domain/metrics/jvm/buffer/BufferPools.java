package com.transwarp.nxms.elasticsearch.domain.metrics.jvm.buffer;


import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 10:05
 */
@Getter
public class BufferPools {
    private Direct direct;
    private Mapped mapped;
}
