package com.lyy.tomcat.manager;

import com.lyy.tomcat.constant.TomcatMetrics;
import com.lyy.tomcat.jmx.TomcatJmx;
import com.lyy.tomcat.model.dynamicMetric.HostDynamicMetric;
import com.lyy.tomcat.model.dynamicMetric.ProtocolDynamicMetric;
import com.lyy.tomcat.model.dynamicMetric.ThreadPoolDynamicMetric;
import com.lyy.tomcat.model.metric.TomcatNodeDetail;
import com.lyy.tomcat.model.metric.TomcatNodeMetric;
import lombok.extern.slf4j.Slf4j;

import javax.management.MBeanServerConnection;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        try(TomcatJmx jmx = new TomcatJmx(this.host, this.port)){
            jmx.connect();
            MBeanServerConnection mbsc = jmx.getConnection();
            TomcatNodeMetric tomcatNodeMetric = TomcatMetrics.getTomcatNodeMetric(mbsc);
            setTomcatNodeDetail(mbsc,tomcatNodeMetric);
            return tomcatNodeMetric;
        }catch(Exception e){
            log.error(e.getMessage());
            return new TomcatNodeMetric();
        }



    }

    public HostDynamicMetric getHostDynamicMetric() {
        try(TomcatJmx jmx = new TomcatJmx(this.host, this.port)){
            jmx.connect();
            MBeanServerConnection mbsc = jmx.getConnection();
            return TomcatMetrics.getHostAttribute(mbsc);
        }catch(Exception e){
            log.error(e.getMessage());
            return new HostDynamicMetric();
        }
    }

    public List<ProtocolDynamicMetric> getProtocolDynamicMetric() {
        try(TomcatJmx jmx = new TomcatJmx(this.host, this.port)){
            jmx.connect();
            MBeanServerConnection mbsc = jmx.getConnection();
            return TomcatMetrics.getProtocolAttribute(mbsc);
        }catch(Exception e){
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ThreadPoolDynamicMetric> getThreadPoolDynamicMetric() {
        try(TomcatJmx jmx = new TomcatJmx(this.host, this.port)){
            jmx.connect();
            MBeanServerConnection mbsc = jmx.getConnection();
            return TomcatMetrics.getThreadPoolAttribute(mbsc);
        }catch(Exception e){
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    private void setTomcatNodeDetail(MBeanServerConnection mbsc, TomcatNodeMetric node) {
        TomcatNodeDetail nodeDetail = new TomcatNodeDetail();
        nodeDetail.setAjpProtocolMetric(TomcatMetrics.getAjpProtocolMetric(mbsc));
        nodeDetail.setHttpProtocolMetric(TomcatMetrics.getHttpProtocolMetric(mbsc));
        nodeDetail.setEngineMetric(TomcatMetrics.getEngineMetric(mbsc));
        nodeDetail.setServerMetric(TomcatMetrics.getServerInfo(mbsc));
        nodeDetail.setHostMetric(TomcatMetrics.getHostMetric(mbsc));
        nodeDetail.setServletMetricMap(TomcatMetrics.getServletMetricMap(mbsc));
        nodeDetail.setThreadPoolMetricMap(TomcatMetrics.getThreadPoolMap(mbsc));
        node.setTomcatNodeDetail(nodeDetail);
    }


    public Boolean testNodeConnect() {
        try(TomcatJmx jmx = new TomcatJmx(this.host, this.port)){
            jmx.connect();
            return jmx.getConnector() != null;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
}
