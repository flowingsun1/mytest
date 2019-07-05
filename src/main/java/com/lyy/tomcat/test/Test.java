package com.lyy.tomcat.test;

import com.lyy.tomcat.constant.TomcatMetrics;
import com.lyy.tomcat.jmx.TomcatJmx;
import com.lyy.tomcat.manager.TomcatManager;
import com.lyy.tomcat.manager.TomcatNodeManager;
import com.lyy.tomcat.model.dynamicMetric.HostDynamicMetric;
import com.lyy.tomcat.model.metric.HostMetric;
import com.lyy.tomcat.model.metric.TomcatNodeMetric;
import lombok.extern.slf4j.Slf4j;

import javax.management.MBeanServerConnection;


/**
 * @Author: lyy
 * @Date: 2019/5/13 15:31
 */
@Slf4j
public class Test {
    public static void main(String[] args) {
//        TomcatManager tomcatManager = new TomcatManager("192.168.1.6:8888,192.168.1.6:8999");
//        boolean connect = tomcatManager.testTomcatConnect();
//        System.out.println(connect);
//        log.info(String.valueOf(connect));
//        log.debug("hahha");

        TomcatNodeManager tomcatNodeManager = new TomcatNodeManager("192.168.1.6","8888",null,null,null);
        TomcatNodeMetric metric = tomcatNodeManager.getNodeMetric();
        HostDynamicMetric sds = tomcatNodeManager.getHostDynamicMetric();
        System.out.println(1);

    }
}
