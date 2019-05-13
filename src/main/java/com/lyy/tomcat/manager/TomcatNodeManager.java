package com.lyy.tomcat.manager;

import com.lyy.tomcat.constant.TomcatMetrics;
import com.lyy.tomcat.jmx.TomcatJmx;
import com.lyy.tomcat.model.dynamicMetric.HostDynamicMetric;
import com.lyy.tomcat.model.dynamicMetric.ProtocolDynamicMetric;
import com.lyy.tomcat.model.dynamicMetric.ThreadPoolDynamicMetric;
import com.lyy.tomcat.model.metric.TomcatNodeDetail;
import com.lyy.tomcat.model.metric.TomcatNodeMetric;

import javax.management.MBeanServerConnection;
import java.util.List;

public class TomcatNodeManager {

    private String host;

    private String port;

    private String jmxUser;

    private String jmxPass;

    private Boolean jmxSsl;


    public TomcatNodeManager(String host, String port, String jmxPass, String jmxUser, Boolean jmxSsl) {
        this.host = host;
        this.port = port;
        this.jmxPass = jmxPass;
        this.jmxSsl = jmxSsl;
        this.jmxUser = jmxUser;
    }


    public TomcatNodeMetric getNodeMetric() {
        TomcatJmx jmx = new TomcatJmx(this.host, this.port);
        jmx.connect();
        MBeanServerConnection mbsc = jmx.getConnection();
        TomcatNodeMetric node = setTomcatNodeDetail(mbsc, TomcatMetrics.getTomcatNodeMetric(mbsc));
        jmx.close();
        return node;
    }

    public HostDynamicMetric getHostDynamicMetric() {
        TomcatJmx jmx = new TomcatJmx(this.host, this.port);
        jmx.connect();
        MBeanServerConnection mbsc = jmx.getConnection();
        HostDynamicMetric hostDynamic = TomcatMetrics.getHostAttribute(mbsc);
        jmx.close();
        return hostDynamic;
    }

    public List<ProtocolDynamicMetric> getProtocolDynamicMetric() {
        TomcatJmx jmx = new TomcatJmx(this.host, this.port);
        jmx.connect();
        MBeanServerConnection mbsc = jmx.getConnection();
        List<ProtocolDynamicMetric> protocolDynamicList = TomcatMetrics.getProtocolAttribute(mbsc);
        jmx.close();
        return protocolDynamicList;
    }

    public List<ThreadPoolDynamicMetric> getThreadPoolDynamicMetric() {
        TomcatJmx jmx = new TomcatJmx(this.host, this.port);
        jmx.connect();
        MBeanServerConnection mbsc = jmx.getConnection();
        List<ThreadPoolDynamicMetric> threadPoolList = TomcatMetrics.getThreadPoolAttribute(mbsc);
        jmx.close();
        return threadPoolList;
    }

    private TomcatNodeMetric setTomcatNodeDetail(MBeanServerConnection mbsc, TomcatNodeMetric node) {
        TomcatNodeDetail nodeDetail = new TomcatNodeDetail();
        nodeDetail.setAjpProtocolMetric(TomcatMetrics.getAjpProtocolMetric(mbsc));
        nodeDetail.setHttpProtocolMetric(TomcatMetrics.getHttpProtocolMetric(mbsc));
        nodeDetail.setEngineMetric(TomcatMetrics.getEngineMetric(mbsc));
        nodeDetail.setServerMetric(TomcatMetrics.getServerInfo(mbsc));
        nodeDetail.setHostMetric(TomcatMetrics.getHostMetric(mbsc));
        nodeDetail.setServletMetricMap(TomcatMetrics.getServletMetricMap(mbsc));
        nodeDetail.setThreadPoolMetricMap(TomcatMetrics.getThreadPoolMap(mbsc));
        node.setTomcatNodeDetail(nodeDetail);
        return node;
    }


    public Boolean testNodeConnect() {
        TomcatJmx jmx = new TomcatJmx(this.host, this.port);
        jmx.connect();
        boolean connect = jmx.getConnector() != null;
        jmx.close();
        return connect;
    }


}
