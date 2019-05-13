package com.lyy.tomcat.model.metric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServletMetric {
    //异步支持
    private Boolean asyncSupported;
    //类加载时间
    private Integer classLoadTime;
    //分配计数
    private Integer countAllocated;
    //错误统计
    private Integer errorCount;
    //最大实例
    private Integer maxInstances;

    //最大时间
    private Long maxTime;
    //最小时间
    private Long minTime;
    //处理时间
    private Long processingTime;
    //请求数
    private Integer requestCount;
    //状态
    private String stateName;
}
