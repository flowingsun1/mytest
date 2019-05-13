package com.lyy.tomcat.model.metric;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TomcatNodeMetric {
    private String host;

    private String jmxPort;

    private String clusterName;

    private String instance;


    private Map<String,String> protocolPortMap;

    private TomcatNodeDetail tomcatNodeDetail;
}
