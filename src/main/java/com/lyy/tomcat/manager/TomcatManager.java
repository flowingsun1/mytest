package com.lyy.tomcat.manager;

import com.lyy.tomcat.constant.TomcatMetrics;
import com.lyy.tomcat.jmx.TomcatJmx;
import com.lyy.tomcat.model.metric.TomcatCluster;
import lombok.extern.slf4j.Slf4j;

import javax.management.MBeanServerConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class TomcatManager {
    private String jmxConnect;

    private static final int defaultPoolSize = 5;

    private static final int defaultWaitTime = 3;

    private String host;

    private String port;

    private String[] connectArray;

    public TomcatManager(String jmxConnect) {
        this.jmxConnect = jmxConnect;
        this.connectArray = jmxConnect.split(",");
        String[] needArray = connectArray[0].split(":");
        this.host = needArray[0];
        this.port = needArray[1];
    }

    public TomcatCluster getClusterMetric() {
        try(TomcatJmx jmx = new TomcatJmx(this.host, this.port)){
            jmx.connect();
            MBeanServerConnection mbsc = jmx.getConnection();
            return TomcatMetrics.getTomcatClusterMetrics(mbsc);
        }catch (Exception e){
            log.error(e.getMessage());
            return new TomcatCluster();
        }

    }

    public Boolean testTomcatConnect() {
        try {
            ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(defaultPoolSize);
            List<Future<Boolean>> results = new ArrayList<>();
            for (String connect : this.connectArray) {
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
            log.error(e.getMessage());
            return false;
        }
    }

    public List<String> getInstanceList() {
        List<String> list = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(defaultPoolSize);
        List<Future<String>> results = new ArrayList<>();
        for (String connect : this.connectArray) {
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
                log.error(e.getMessage());
            }
        }
        pool.shutdown();
        return list;
    }

    private Callable<Boolean> testConnectCall(final String connect) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() {
                String[] hostInfo = connect.split(":");
                try(TomcatJmx jmx = new TomcatJmx(hostInfo[0], hostInfo[1])){
                    jmx.connect();
                    return jmx.getConnector() != null;
                }catch(Exception e){
                    log.error(e.getMessage());
                    return false;
                }

            }
        };
    }

    private Callable<String> instanceCall(final String connect) {
        return new Callable<String>() {
            @Override
            public String call() {
                String[] hostInfo = connect.split(":");
                String instance = "";
                try(TomcatJmx jmx = new TomcatJmx(hostInfo[0], hostInfo[1])){
                    jmx.connect();
                    MBeanServerConnection mbsc = jmx.getConnection();
                    if (jmx.getConnector() != null) {
                        instance = TomcatMetrics.getInstance(mbsc);
                    }
                }catch(Exception e){
                    log.error(e.getMessage());
                }
                return instance;
            }
        };
    }

    public int getAppCount() {
        try(TomcatJmx jmx = new TomcatJmx(this.host, this.port)){
            jmx.connect();
            MBeanServerConnection mbsc = jmx.getConnection();
            return TomcatMetrics.getAppCount(mbsc);
        }catch(Exception e){
            log.error(e.getMessage());
            return 0;
        }


    }


}
