package com.lyy.tomcat.model.metric;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TomcatCluster {
    private String name;
    private String status;
    private Integer sendOptions;
    private Integer startOptions;
    private Boolean heartbeatDetection;
    private Boolean failureNotice;

    public TomcatCluster(String name, String status, Integer sendOptions, Integer startOptions, Boolean heartbeatDetection, Boolean failureNotice) {
        this.name = name;
        this.status = status;
        this.sendOptions = sendOptions;
        this.startOptions = startOptions;
        this.heartbeatDetection = heartbeatDetection;
        this.failureNotice = failureNotice;
    }

    private Integer nodeCount;
    private Integer appCount;
    private String type;




}
