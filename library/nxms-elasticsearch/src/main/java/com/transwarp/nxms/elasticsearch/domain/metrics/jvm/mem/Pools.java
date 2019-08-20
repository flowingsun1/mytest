package com.transwarp.nxms.elasticsearch.domain.metrics.jvm.mem;

import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 10:05
 */
@Getter
class Pools {
    private Young young;
    private Survivor survivor;
    private Old old;
}
