package com.lyy.tomcat.model.metric;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TomcatCluster {
    private String name;
    private String status;
    private Integer sendOptions;
    private Integer startOptions;
    private Boolean heartbeatDetection;
    private Boolean failureNotice;


    private Integer nodeCount;
    private Integer appCount;
    private String type;




}
