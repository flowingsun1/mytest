package com.lyy.tomcat.constant;

import com.lyy.tomcat.model.dynamicMetric.HostDynamicMetric;
import com.lyy.tomcat.model.dynamicMetric.ProtocolDynamicMetric;
import com.lyy.tomcat.model.dynamicMetric.ThreadPoolDynamicMetric;
import com.lyy.tomcat.model.metric.*;

import javax.management.*;
import java.util.*;

public class TomcatMetrics {

    private static String getEngineName(MBeanServerConnection mbsc) {
        String engineName = "";
        try {
            ObjectName objName = new ObjectName("*:type=Engine");
            Set<ObjectName> set = mbsc.queryNames(objName, null);
            for (ObjectName obj : set) {
                engineName = obj.getDomain();
            }
            return engineName;
        } catch (Exception e) {
            return engineName;
        }
    }

    public static TomcatCluster getTomcatClusterMetrics(MBeanServerConnection mbsc) {
        try {
            ObjectName clusterName = new ObjectName(getEngineName(mbsc) + ":type=Cluster");
            AttributeList attributes = mbsc.getAttributes(clusterName, MetricArray.CLUSTER_STR);
            return new TomcatCluster((String) ((Attribute) attributes.get(0)).getValue(), (String) ((Attribute) attributes.get(1)).getValue(),
                    (Integer) ((Attribute) attributes.get(2)).getValue(), (Integer) ((Attribute) attributes.get(3)).getValue(),
                    (Boolean) ((Attribute) attributes.get(4)).getValue(), (Boolean) ((Attribute) attributes.get(5)).getValue());
        } catch (Exception e) {
            return new TomcatCluster();
        }
    }

    public static TomcatNodeMetric getTomcatNodeMetric(MBeanServerConnection mbsc) {
        try {
            String engineName = getEngineName(mbsc);
            ObjectName engineObjName = new ObjectName(engineName + ":type=Engine");
            TomcatNodeMetric nodeMetric = new TomcatNodeMetric();
            nodeMetric.setInstance((String) mbsc.getAttribute(engineObjName, "jvmRoute"));
            Map<String, String> portProtocolMap = getPortProtocolMap(mbsc, engineName);
            nodeMetric.setProtocolPortMap(portProtocolMap);
            return nodeMetric;
        } catch (Exception e) {
            return new TomcatNodeMetric();
        }
    }

    public static String getInstance(MBeanServerConnection mbsc) {
        try {
            ObjectName engineObjName = new ObjectName(getEngineName(mbsc) + ":type=Engine");
            return (String) mbsc.getAttribute(engineObjName, "jvmRoute");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static ServerMetric getServerInfo(MBeanServerConnection mbsc) {
        try {
            ObjectName serverName = new ObjectName(getEngineName(mbsc) + ":type=Server");
            AttributeList attributes = mbsc.getAttributes(serverName, MetricArray.SERVER_STR);
            return new ServerMetric((String) ((Attribute) attributes.get(0)).getValue(), (String) ((Attribute) attributes.get(1)).getValue(),
                    (Integer) ((Attribute) attributes.get(2)).getValue(), (String) ((Attribute) attributes.get(3)).getValue(),
                    (String) ((Attribute) attributes.get(4)).getValue(), (String) ((Attribute) attributes.get(5)).getValue(),
                    (String) ((Attribute) attributes.get(6)).getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerMetric();
        }
    }

    public static EngineMetric getEngineMetric(MBeanServerConnection mbsc) {
        try {
            ObjectName engineName = new ObjectName(getEngineName(mbsc) + ":type=Engine");
            AttributeList attributes = mbsc.getAttributes(engineName, MetricArray.ENGINE_STR);
            return new EngineMetric((Integer) ((Attribute) attributes.get(0)).getValue(), ((Attribute) attributes.get(1)).getValue().toString(),
                    (String) ((Attribute) attributes.get(2)).getValue(), (String) ((Attribute) attributes.get(3)).getValue(),
                    (String) ((Attribute) attributes.get(4)).getValue(), (String) ((Attribute) attributes.get(5)).getValue(),
                    (Boolean) ((Attribute) attributes.get(6)).getValue(), (Integer) ((Attribute) attributes.get(7)).getValue(),
                    (String) ((Attribute) attributes.get(8)).getValue());
        } catch (Exception e) {
            return new EngineMetric();
        }
    }

    public static HostMetric getHostMetric(MBeanServerConnection mbsc) {
        try {
            ObjectName hostObjName = new ObjectName(getEngineName(mbsc) + ":type=Host,host=localhost");
            AttributeList attributes = mbsc.getAttributes(hostObjName, MetricArray.HOST_STR);
            return new HostMetric((String) ((Attribute) attributes.get(0)).getValue(), (Boolean) ((Attribute) attributes.get(1)).getValue(),
                    (Integer) ((Attribute) attributes.get(2)).getValue(), (String) ((Attribute) attributes.get(3)).getValue(),
                    (String) ((Attribute) attributes.get(4)).getValue(), (Boolean) ((Attribute) attributes.get(5)).getValue(),
                    (Boolean) ((Attribute) attributes.get(6)).getValue(), (Boolean) ((Attribute) attributes.get(7)).getValue(),
                    (Boolean) ((Attribute) attributes.get(8)).getValue(), (String) ((Attribute) attributes.get(9)).getValue(),
                    (Boolean) ((Attribute) attributes.get(10)).getValue(), (Integer) ((Attribute) attributes.get(11)).getValue(),
                    (String) ((Attribute) attributes.get(12)).getValue(), (Boolean) ((Attribute) attributes.get(13)).getValue(),
                    (Boolean) ((Attribute) attributes.get(14)).getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return new HostMetric();
        }
    }

    public static AjpProtocolMetric getAjpProtocolMetric(MBeanServerConnection mbsc) {
        try {
            String engineName = getEngineName(mbsc);
            Map<String, String> portProtocolMap = getPortProtocolMap(mbsc, engineName);
            for (Map.Entry<String, String> entry : portProtocolMap.entrySet()) {
                if (entry.getValue().contains("AJP")) {
                    ObjectName objName = new ObjectName(engineName + ":type=ProtocolHandler,port=" + entry.getKey());
                    AttributeList attributes = mbsc.getAttributes(objName, MetricArray.AJP_STR);
                    return new AjpProtocolMetric((Integer) ((Attribute) attributes.get(0)).getValue(), (Integer) ((Attribute) attributes.get(1)).getValue(),
                            (Integer) ((Attribute) attributes.get(2)).getValue(), (Boolean) ((Attribute) attributes.get(3)).getValue(),
                            (Boolean) ((Attribute) attributes.get(4)).getValue(), (Integer) ((Attribute) attributes.get(5)).getValue(),
                            (Integer) ((Attribute) attributes.get(6)).getValue(), (Integer) ((Attribute) attributes.get(7)).getValue(),
                            (Integer) ((Attribute) attributes.get(8)).getValue(), (Integer) ((Attribute) attributes.get(9)).getValue(),
                            (String) ((Attribute) attributes.get(10)).getValue(), (String) ((Attribute) attributes.get(11)).getValue(),
                            (Boolean) ((Attribute) attributes.get(12)).getValue(), (Integer) ((Attribute) attributes.get(13)).getValue(),
                            (Boolean) ((Attribute) attributes.get(14)).getValue(), (Boolean) ((Attribute) attributes.get(15)).getValue());
                }
            }
            return new AjpProtocolMetric();
        } catch (Exception e) {
            e.printStackTrace();
            return new AjpProtocolMetric();
        }
    }

    public static HttpProtocolMetric getHttpProtocolMetric(MBeanServerConnection mbsc) {
        try {
            String engineName = getEngineName(mbsc);
            Map<String, String> portProtocolMap = getPortProtocolMap(mbsc, engineName);
            for (Map.Entry<String, String> entry : portProtocolMap.entrySet()) {
                if (entry.getValue().contains("HTTP")) {
                    ObjectName objName = new ObjectName(engineName + ":type=ProtocolHandler,port=" + entry.getKey());
                    AttributeList attributes = mbsc.getAttributes(objName, MetricArray.HTTP_STR);
                    return new HttpProtocolMetric((Integer) ((Attribute) attributes.get(0)).getValue(), (Integer) ((Attribute) attributes.get(1)).getValue(),
                            (Integer) ((Attribute) attributes.get(2)).getValue(), (String) ((Attribute) attributes.get(3)).getValue(),
                            (Boolean) ((Attribute) attributes.get(4)).getValue(), (Integer) ((Attribute) attributes.get(5)).getValue(),
                            (Integer) ((Attribute) attributes.get(6)).getValue(), (Integer) ((Attribute) attributes.get(7)).getValue(),
                            (Integer) ((Attribute) attributes.get(8)).getValue(), (Integer) ((Attribute) attributes.get(9)).getValue(),
                            (Integer) ((Attribute) attributes.get(10)).getValue(), (String) ((Attribute) attributes.get(11)).getValue(),
                            (String) ((Attribute) attributes.get(12)).getValue(), (Boolean) ((Attribute) attributes.get(13)).getValue(),
                            (Integer) ((Attribute) attributes.get(14)).getValue(), (Boolean) ((Attribute) attributes.get(15)).getValue());
                }
            }
            return new HttpProtocolMetric();
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpProtocolMetric();
        }
    }

    private static ThreadPoolMetric getThreadPoolMetric(MBeanServerConnection mbsc, String threadPoolName, String engineName) {
        try {
            ObjectName objName = new ObjectName(engineName + ":type=ThreadPool,name=" + threadPoolName);
            AttributeList attributes = mbsc.getAttributes(objName, MetricArray.THREADPOOL_STR);
            return new ThreadPoolMetric((Boolean) ((Attribute) attributes.get(0)).getValue(), (Integer) ((Attribute) attributes.get(1)).getValue(),
                    (Integer) ((Attribute) attributes.get(2)).getValue(), (Boolean) ((Attribute) attributes.get(3)).getValue(),
                    (Long) ((Attribute) attributes.get(4)).getValue(), (String) ((Attribute) attributes.get(5)).getValue(),
                    (Integer) ((Attribute) attributes.get(6)).getValue(), (Boolean) ((Attribute) attributes.get(7)).getValue(),
                    (Integer) ((Attribute) attributes.get(8)).getValue(), (Integer) ((Attribute) attributes.get(9)).getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return new ThreadPoolMetric();
        }
    }

    private static ServletMetric getServletMetric(MBeanServerConnection mbsc, String servletName, String engineName) {
        try {
            ObjectName objName = new ObjectName(engineName + ":j2eeType=Servlet,WebModule=//localhost/,name=" + servletName + ",J2EEApplication=none,J2EEServer=none");
            AttributeList attributes = mbsc.getAttributes(objName, MetricArray.SERVLET_STR);
            return new ServletMetric((Boolean) ((Attribute) attributes.get(0)).getValue(), (Integer) ((Attribute) attributes.get(1)).getValue(),
                    (Integer) ((Attribute) attributes.get(2)).getValue(), (Integer) ((Attribute) attributes.get(3)).getValue(),
                    (Integer) ((Attribute) attributes.get(4)).getValue(), (Long) ((Attribute) attributes.get(5)).getValue(),
                    (Long) ((Attribute) attributes.get(6)).getValue(), (Long) ((Attribute) attributes.get(7)).getValue(),
                    (Integer) ((Attribute) attributes.get(8)).getValue(), (String) ((Attribute) attributes.get(9)).getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return new ServletMetric();
        }
    }

    public static Map<String, ServletMetric> getServletMetricMap(MBeanServerConnection mbsc) {
        try {
            String engineName = getEngineName(mbsc);
            Map<String, ServletMetric> servletMetricMap = new HashMap<>();
            ObjectName objectName = new ObjectName(engineName + ":j2eeType=Servlet,*");
            Set<ObjectName> servletSet = mbsc.queryNames(objectName, null);
            for (ObjectName obj : servletSet) {
                String servletName = obj.getKeyProperty("name");
                ServletMetric servletMetric = getServletMetric(mbsc, servletName, engineName);
                servletMetricMap.put(servletName, servletMetric);
            }
            return servletMetricMap;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private static Map<String, String> getPortProtocolMap(MBeanServerConnection mbsc, String engineName) {
        try {
            ObjectName ajpObjName = new ObjectName(engineName + ":type=Connector,*");
            Set<ObjectName> set = mbsc.queryNames(ajpObjName, null);
            Map<String, String> protocolMap = new LinkedHashMap<>();
            for (ObjectName obj : set) {
                protocolMap.put(obj.getKeyProperty("port"), (String) mbsc.getAttribute(obj, "protocol"));
            }
            return protocolMap;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


    public static Map<String, ThreadPoolMetric> getThreadPoolMap(MBeanServerConnection mbsc) {
        try {
            String engineName = getEngineName(mbsc);
            Map<String, ThreadPoolMetric> threadPoolMap = new HashMap<>();
            ObjectName objName = new ObjectName(engineName + ":type=ThreadPool,*");
            Set<ObjectName> set = mbsc.queryNames(objName, null);
            for (ObjectName obj : set) {
                String threadPoolName = obj.getKeyProperty("name");
                ThreadPoolMetric threadPoolMetric = getThreadPoolMetric(mbsc, threadPoolName, engineName);
                threadPoolMap.put(threadPoolName, threadPoolMetric);
            }
            return threadPoolMap;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


    /**
     * @return 主机信息动态指标
     */
    public static HostDynamicMetric getHostAttribute(MBeanServerConnection mbsc) {
        try {
            ObjectName objName = new ObjectName(getEngineName(mbsc) + ":type=Manager,context=/,host=localhost");
            AttributeList attributes = mbsc.getAttributes(objName, MetricArray.HOST_DYNAMIC_METRIC);
            HostDynamicMetric metrics = new HostDynamicMetric();
            metrics.setActiveSessions((Integer) ((Attribute) attributes.get(0)).getValue());
            metrics.setCounterNoStateTransfered((Integer) ((Attribute) attributes.get(1)).getValue());
            metrics.setExpiredSessions((Long) ((Attribute) attributes.get(2)).getValue());
            metrics.setMaxActive((Integer) ((Attribute) attributes.get(3)).getValue());

            metrics.setMaxActiveSessions((Integer) ((Attribute) attributes.get(4)).getValue());
            metrics.setRejectedSessions((Integer) ((Attribute) attributes.get(5)).getValue());
            metrics.setSessionAverageAliveTime((Integer) ((Attribute) attributes.get(6)).getValue());
            metrics.setSessionMaxAliveTime((Integer) ((Attribute) attributes.get(7)).getValue());
            metrics.setSessionReplaceCounter((Long) ((Attribute) attributes.get(8)).getValue());
            return metrics;
        } catch (Exception e) {
            return new HostDynamicMetric();
        }
    }

    /**
     * @return 端口协议动态指标
     */
    public static List<ProtocolDynamicMetric> getProtocolAttribute(MBeanServerConnection mbsc) {
        try {
            String engineName = getEngineName(mbsc);
            List<ProtocolDynamicMetric> protocolList = new ArrayList<>();
            ObjectName objName = new ObjectName(engineName + ":type=ThreadPool,*");
            Set<ObjectName> set = mbsc.queryNames(objName, null);
            for (ObjectName obj : set) {
                String threadPoolName = obj.getKeyProperty("name");
                ProtocolDynamicMetric protocolDynamicMetric = new ProtocolDynamicMetric();
                ObjectName protocolObjName = new ObjectName(engineName + ":type=GlobalRequestProcessor,name=" + threadPoolName);
                AttributeList attributes = mbsc.getAttributes(protocolObjName, MetricArray.PROTOCOL_DYNAMIC_METRIC);
                protocolDynamicMetric.setProtocolName(threadPoolName);
                protocolDynamicMetric.setBytesReceived((Long) ((Attribute) attributes.get(0)).getValue());
                protocolDynamicMetric.setBytesSent((Long) ((Attribute) attributes.get(1)).getValue());
                protocolDynamicMetric.setErrorCount((Integer) ((Attribute) attributes.get(2)).getValue());
                protocolDynamicMetric.setProcessingTime((Long) ((Attribute) attributes.get(3)).getValue());
                protocolDynamicMetric.setRequestCount((Integer) ((Attribute) attributes.get(4)).getValue());
                protocolList.add(protocolDynamicMetric);
            }
            return protocolList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * @return 线程池动态指标
     */
    public static List<ThreadPoolDynamicMetric> getThreadPoolAttribute(MBeanServerConnection mbsc) {
        try {
            String engineName = getEngineName(mbsc);
            ObjectName objName = new ObjectName(engineName + ":type=ThreadPool,*");
            Set<ObjectName> set = mbsc.queryNames(objName, null);
            List<ThreadPoolDynamicMetric> list = new ArrayList<>();
            for (ObjectName obj : set) {
                String threadPoolName = obj.getKeyProperty("name");
                ObjectName threadPoolObject = new ObjectName(engineName + ":type=ThreadPool,name=" + threadPoolName);
                AttributeList attributes = mbsc.getAttributes(threadPoolObject, MetricArray.THREADPOOL_DYNAMIC_METRIC);
                ThreadPoolDynamicMetric threadPoolDynamicMetric = new ThreadPoolDynamicMetric();
                threadPoolDynamicMetric.setThreadPoolName(threadPoolName);
                threadPoolDynamicMetric.setConnectionCount((Long) ((Attribute) attributes.get(0)).getValue());
                threadPoolDynamicMetric.setAcceptorThreadCount((Integer) ((Attribute) attributes.get(1)).getValue());
                threadPoolDynamicMetric.setCurrentThreadCount((Integer) ((Attribute) attributes.get(2)).getValue());
                threadPoolDynamicMetric.setCurrentThreadsBusy((Integer) ((Attribute) attributes.get(3)).getValue());
                list.add(threadPoolDynamicMetric);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 获取节点应用数
     */
    public static int getAppCount(MBeanServerConnection mbsc) {
        try {
            ObjectName objName = new ObjectName(getEngineName(mbsc) + ":type=Manager,*");
            Set<ObjectName> set = mbsc.queryNames(objName, null);
            return set.size();
        } catch (Exception e) {
            return 0;
        }
    }


}
