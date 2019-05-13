package com.lyy.tomcat.model.metric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AjpProtocolMetric {
    //接收数
    private Integer acceptCount;
    //接收线程数
    private Integer acceptorThreadCount;
    //接受线程优先级
    private Integer acceptorThreadPriority;
    //AJP刷新
    private Boolean ajpFlush;
    //APR必需
    private Boolean aprRequired;

    //端口
    private Integer port;
    //最大连接数
    private Integer maxConnections;
    //最大请求头数
    private Integer maxHeaderCount;
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
    //线程优先级
    private Integer threadPriority;
    //Tomcat认证
    private Boolean tomcatAuthentication;
    //Tomcat授权
    private Boolean tomcatAuthorization;


}
