package com.lyy.tomcat.model.metric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThreadPoolMetric {
    //ALPN支持
    private Boolean alpnSupported;
    //当前线程数
    private Integer currentThreadCount;
    //当前繁忙线程数
    private Integer currentThreadsBusy;
    //守护进程
    private Boolean daemon;
    //执行终止超时时间(毫秒)
    private Long executorTerminationTimeoutMillis;

    //建模类型
    private String modelerType;
    //端口
    private Integer port;
    //正在执行
    private Boolean running;
    //轮询线程数
    private Integer pollerThreadCount;
    //轮询器线程优先级
    private Integer pollerThreadPriority;
}
