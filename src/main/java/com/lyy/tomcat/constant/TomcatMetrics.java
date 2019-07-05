package com.lyy.tomcat.constant;

import com.lyy.tomcat.model.dynamicMetric.HostDynamicMetric;
import com.lyy.tomcat.model.dynamicMetric.ProtocolDynamicMetric;
import com.lyy.tomcat.model.dynamicMetric.ThreadPoolDynamicMetric;
import com.lyy.tomcat.model.metric.*;
import lombok.extern.slf4j.Slf4j;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.lang.reflect.Field;
import java.util.*;
@Slf4j
public class TomcatMetrics {

    public static String getEngineName(MBeanServerConnection mbsc) {
        String engineName = "";
        try {
            ObjectName objName = new ObjectName("*:type=Engine");
            Set<ObjectName> set = mbsc.queryNames(objName, null);
            for (ObjectName obj : set) {
                engineName = obj.getDomain();
            }
            return engineName;
        } catch (Exception e) {
            log.error(e.getMessage());
            return engineName;
        }
    }

    public static TomcatCluster getTomcatClusterMetrics(MBeanServerConnection mbsc) {
        try {
            ObjectName clusterName = new ObjectName(getEngineName(mbsc) + ":type=Cluster");
            AttributeList attributes = mbsc.getAttributes(clusterName, MetricArray.CLUSTER_STR);
            return convertInstance(attributes,new TomcatCluster());
        } catch (Exception e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());
            return new TomcatNodeMetric();
        }
    }

    public static String getInstance(MBeanServerConnection mbsc) {
        try {
            ObjectName engineObjName = new ObjectName(getEngineName(mbsc) + ":type=Engine");
            return (String) mbsc.getAttribute(engineObjName, "jvmRoute");
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }

    public static ServerMetric getServerInfo(MBeanServerConnection mbsc) {
        try {
            ObjectName serverName = new ObjectName(getEngineName(mbsc) + ":type=Server");
            AttributeList attributes = mbsc.getAttributes(serverName, MetricArray.SERVER_STR);
            return convertInstance(attributes,new ServerMetric());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ServerMetric();
        }
    }

    public static EngineMetric getEngineMetric(MBeanServerConnection mbsc) {
        try {
            ObjectName engineName = new ObjectName(getEngineName(mbsc) + ":type=Engine");
            AttributeList attributes = mbsc.getAttributes(engineName, MetricArray.ENGINE_STR);
            EngineMetric engineMetric =  convertInstance(attributes,new EngineMetric());
            return engineMetric;
        } catch (Exception e) {
            log.error(e.getMessage());
            return new EngineMetric();
        }
    }

    public static HostMetric getHostMetric(MBeanServerConnection mbsc) {
        try {
            ObjectName objName = new ObjectName(getEngineName(mbsc) + ":type=Host,host=*");
            Set<ObjectName> set = mbsc.queryNames(objName, null);
            ObjectName hostObjectName = set.iterator().next() ;
            AttributeList attributes = mbsc.getAttributes(hostObjectName, MetricArray.HOST_STR);
            return convertInstance(attributes,new HostMetric());
        } catch (Exception e) {
            log.error(e.getMessage());
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
                    return convertInstance(attributes,new AjpProtocolMetric());
                }
            }
            return new AjpProtocolMetric();
        } catch (Exception e) {
            log.error(e.getMessage());
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
                    return convertInstance(attributes,new HttpProtocolMetric());
                }
            }
            return new HttpProtocolMetric();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new HttpProtocolMetric();
        }
    }

    private static ThreadPoolMetric getThreadPoolMetric(MBeanServerConnection mbsc, String threadPoolName, String engineName) {
        try {
            ObjectName objName = new ObjectName(engineName + ":type=ThreadPool,name=" + threadPoolName);
            AttributeList attributes = mbsc.getAttributes(objName, MetricArray.THREADPOOL_STR);
            return convertInstance(attributes,new ThreadPoolMetric());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ThreadPoolMetric();
        }
    }

    private static <T> T convertInstance(AttributeList attributeList,T t) throws IllegalAccessException {
        for (Attribute attribute : attributeList.asList()) {
            String name = attribute.getName();
            Object value = attribute.getValue();
            Field  field = null;
            try {
                field = t.getClass().getDeclaredField(name);
            } catch (NoSuchFieldException e) {
            }
            if(field!= null){
                field.setAccessible(true);
                field.set(t, value);
            }
        }
        return t;
    }

    private static ServletMetric getServletMetric(MBeanServerConnection mbsc, String servletName, String engineName) {
        try {
            ObjectName objName = new ObjectName(engineName + ":j2eeType=Servlet,WebModule=*,name=" + servletName + ",J2EEApplication=none,J2EEServer=none");
            Set<ObjectName> set = mbsc.queryNames(objName, null);
            ObjectName servlet = set.iterator().next();
            AttributeList attributes = mbsc.getAttributes(servlet, MetricArray.SERVLET_STR);
            ServletMetric servletMetric = new ServletMetric();
            return convertInstance(attributes,servletMetric);
        } catch (Exception e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());
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
            log.error(e.getMessage());
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
            log.error(e.getMessage());
            return new HashMap<>();
        }
    }


    /**
     * @return 主机信息动态指标
     */
    public static HostDynamicMetric getHostAttribute(MBeanServerConnection mbsc) {
        try {
            ObjectName objName = new ObjectName(getEngineName(mbsc) + ":type=Manager,context=*,host=*");
            Set<ObjectName> set = mbsc.queryNames(objName, null);
            ObjectName hostDynamicObjName = set.iterator().next();
            AttributeList attributes = mbsc.getAttributes(hostDynamicObjName, MetricArray.HOST_DYNAMIC_METRIC);
            HostDynamicMetric metrics = new HostDynamicMetric();
            return convertInstance(attributes,metrics);
        } catch (Exception e) {
            log.error(e.getMessage());
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
                protocolList.add(convertInstance(attributes,protocolDynamicMetric));
            }
            return protocolList;
        } catch (Exception e) {
            log.error(e.getMessage());
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
                list.add(convertInstance(attributes,threadPoolDynamicMetric));
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());
            return 0;
        }
    }


}
