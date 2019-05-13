package com.lyy.tomcat.model.metric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HttpProtocolMetric {
    //接收数
    private Integer acceptCount;
    //接收线程数
    private Integer acceptorThreadCount;
    //接受线程优先级
    private Integer acceptorThreadPriority;
    //算法
    private String algorithm;
    //允许主机请求头不匹配
    private Boolean allowHostHeaderMismatch;

    //端口
    private Integer port;
    //最大连接数
    private Integer maxConnections;
    //最大请求头数
    private Integer maxHeaderCount;
    //最大HTTP请求头大小
    private Integer maxHttpHeaderSize;
    //最大线程数
    private Integer maxThreads;

    //最小备用线程
    private Integer minSpareThreads;
    //建模类型
    private String modelerType;
    //名称
    private String name;
    //tcp无延迟
    private Boolean tcpNoDelay;
    //最大扩展大小
    private Integer maxExtensionSize;
    //拒绝非法请求头名称
    private Boolean rejectIllegalHeaderName;
}
