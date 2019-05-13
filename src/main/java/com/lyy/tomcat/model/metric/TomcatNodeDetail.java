package com.lyy.tomcat.model.metric;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @Author: lyy
 * @Date: 2019/1/16 10:43
 */
@Setter
@Getter
public class TomcatNodeDetail {
    private AjpProtocolMetric ajpProtocolMetric;
    private EngineMetric engineMetric;
    private HostMetric hostMetric;
    private HttpProtocolMetric httpProtocolMetric;
    private ServerMetric serverMetric;
    private Map<String,ServletMetric> servletMetricMap;
    private Map<String,ThreadPoolMetric> threadPoolMetricMap;
}
