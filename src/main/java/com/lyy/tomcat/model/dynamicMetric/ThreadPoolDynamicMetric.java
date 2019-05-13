package com.lyy.tomcat.model.dynamicMetric;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: lyy
 * @Date: 2019/1/16 16:09
 */
@Setter
@Getter
public class ThreadPoolDynamicMetric {

    //连接数
    private Long connectionCount;
    //接收线程数
    private Integer acceptorThreadCount;
    //当前线程数
    private Integer currentThreadCount;
    //当前占线线程数
    private Integer currentThreadsBusy;

    private String threadPoolName;

    //对应tomcat实例
    private String instance;
}
