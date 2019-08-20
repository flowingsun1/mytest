package com.transwarp.nxms.elasticsearch.domain.metrics.thread;

import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/16 11:04
 */
@Getter
class Management {
    private String threads;
    private String queue;
    private String active;
    private String rejected;
    private String largest;
    private String completed;
}
