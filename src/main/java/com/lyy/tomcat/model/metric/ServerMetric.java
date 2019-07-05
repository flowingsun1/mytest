package com.lyy.tomcat.model.metric;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServerMetric {
    //地址
    private String address;
    //建模类型
    private String modelerType;
    //端口
    private Integer port;
    //启动时间
    private String serverBuilt;
    //版本
    private String serverInfo;
    //关闭
    private String shutdown;
    //状态
    private String stateName;
}
