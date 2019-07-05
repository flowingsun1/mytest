package com.lyy.tomcat.model.metric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostMetric {
    //应用程序库
    private String appBase;
    //自动部署
    private Boolean autoDeploy;
    //后台处理延迟
    private Integer backgroundProcessorDelay;
    //配置类
    private String configClass;
    //上下文类
    private String contextClass;

    //xml复制
    private Boolean copyXML;
    //是否自动创建目录
    private Boolean createDirs;
    //启动时部署
    private Boolean deployOnStartup;
    //部署xml
    private Boolean deployXML;
    //主机名
    private String name;

    //启动子代
    private Boolean startChildren;
    //线程池内线程数
    private Integer startStopThreads;
    //状态
    private String stateName;
    //不部署旧版本
    private Boolean undeployOldVersions;
    //解压war包
    private Boolean unpackWARs;
}
