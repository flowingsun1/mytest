package com.lyy.tomcat.model.dynamicMetric;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: lyy
 * @Date: 2019/1/16 16:09
 */
@Setter
@Getter
public class ProtocolDynamicMetric {

    //接收字节
    private Long bytesReceived;
    //发送字节
    private Long bytesSent;
    //错误统计数
    private Integer errorCount;
    //加工时长
    private Long processingTime;
    //请求统计数
    private Integer requestCount;

    private String protocolName;

}
