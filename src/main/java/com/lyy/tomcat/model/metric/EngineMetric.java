package com.lyy.tomcat.model.metric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EngineMetric {
    //后台进程延迟
    private Integer backgroundProcessorDelay;
    //基准目录
    private String catalinaBase;
    //默认主机
    private String defaultHost;
    //jvm路由
    private String jvmRoute;
    //建模类型
    private String modelerType;

    //名称
    private String name;
    //启动子代
    private Boolean startChildren;
    //线程池内线程数
    private Integer startStopThreads;
    //状态
    private String stateName;
}
