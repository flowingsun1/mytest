package com.lyy.tomcat.manager;

import com.lyy.tomcat.constant.TomcatMetrics;
import com.lyy.tomcat.jmx.TomcatJmx;
import com.lyy.tomcat.model.metric.TomcatCluster;

import javax.management.MBeanServerConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TomcatManager {
    private String jmxConnect;

    private static final int defaultPoolSize = 5;

    private static final int defaultWaitTime = 3;


    public TomcatManager(String jmxConnect) {
        this.jmxConnect = jmxConnect;
    }


    public Boolean testTomcatConnect() {
        try {
            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(defaultPoolSize);
            String[] connects = this.jmxConnect.split(",");
            List<Future<Boolean>> results = new ArrayList<>();
            for (String connect : connects) {
                FutureTask<Boolean> futureTask = new FutureTask<>(testConnectCall(connect));
                pool.execute(futureTask);
                results.add(futureTask);
            }
            for (Future<Boolean> fs : results) {
                if (!fs.get(defaultWaitTime, TimeUnit.SECONDS)) {
                    return false;
                }
            }
            pool.shutdown();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public TomcatCluster getClusterMetric() {
        String host = this.jmxConnect.split(",")[0].split(":")[0];
        String port = this.jmxConnect.split(",")[0].split(":")[1];
        TomcatJmx jmx = new TomcatJmx(host, port);
        jmx.connect();
        MBeanServerConnection mbsc = jmx.getConnection();
        TomcatCluster tomcatClusterMetric = TomcatMetrics.getTomcatClusterMetrics(mbsc);
        jmx.close();
        return tomcatClusterMetric;
    }


    private Callable<Boolean> testConnectCall(final String connectString) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() {
                String[] hostInfo = connectString.split(":");
                TomcatJmx jmx = new TomcatJmx(hostInfo[0], hostInfo[1]);
                jmx.connect();
                boolean connect = jmx.getConnector() != null;
                jmx.close();
                return connect;
            }
        };
    }

    private Callable<String> instanceCall(final String connect) {
        return new Callable<String>() {
            @Override
            public String call() {
                String[] hostInfo = connect.split(":");
                TomcatJmx jmx = new TomcatJmx(hostInfo[0], hostInfo[1]);
                jmx.connect();
                MBeanServerConnection mbsc = jmx.getConnection();
                String instance = "";
                if (jmx.getConnector() != null) {
                    instance = TomcatMetrics.getInstance(mbsc);
                }
                jmx.close();
                return instance;
            }
        };

    }

    public List<String> getInstanceList() {
        List<String> list = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(defaultPoolSize);
        String[] connects = this.jmxConnect.split(",");
        List<Future<String>> results = new ArrayList<>();
        for (String connect : connects) {
            FutureTask<String> futureTask = new FutureTask<>(instanceCall(connect));
            pool.execute(futureTask);
            results.add(futureTask);
        }
        for (Future<String> fs : results) {
            try {
                String str = fs.get(defaultWaitTime, TimeUnit.SECONDS);
                if (!"".equals(str)) {
                    list.add(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();
        return list;
    }

    public int getAppCount() {
        String host = this.jmxConnect.split(",")[0].split(":")[0];
        String port = this.jmxConnect.split(",")[0].split(":")[1];
        TomcatJmx jmx = new TomcatJmx(host, port);
        jmx.connect();
        MBeanServerConnection mbsc = jmx.getConnection();
        int appCount = TomcatMetrics.getAppCount(mbsc);
        jmx.close();
        return appCount;
    }


}
