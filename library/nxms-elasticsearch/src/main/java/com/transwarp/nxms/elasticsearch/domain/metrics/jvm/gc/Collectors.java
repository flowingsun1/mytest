package com.transwarp.nxms.elasticsearch.domain.metrics.jvm.gc;


import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 10:05
 */
@Getter
class Collectors {
    private Young young;
    private Old old;
}
