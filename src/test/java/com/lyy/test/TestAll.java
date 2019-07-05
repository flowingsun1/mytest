package com.lyy.test;

import com.lyy.tomcat.constant.AcquisitionFrequency;
import com.lyy.tomcat.constant.MetricArray;
import com.lyy.tomcat.constant.TomcatMetrics;
import com.lyy.tomcat.jmx.TomcatJmx;
import com.lyy.tomcat.manager.TomcatManager;
import com.lyy.tomcat.manager.TomcatNodeManager;
import com.lyy.tomcat.model.dynamicMetric.HostDynamicMetric;
import com.lyy.tomcat.model.dynamicMetric.ProtocolDynamicMetric;
import com.lyy.tomcat.model.dynamicMetric.ThreadPoolDynamicMetric;
import com.lyy.tomcat.model.metric.TomcatNodeMetric;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.xml.transform.Source;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: lyy
 * @Date: 2019/5/14 15:03
 */
public class TestAll {
    @Test
    public void sss() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("jmxConnect", "192.168.1.6:8888,192.168.1.6:8999");
//        Map<String,String> map = new HashMap<>();
//        map.put("jmxConnect", "192.168.1.95:8888,192.168.1.95:8999");
        boolean connect = restTemplate.postForObject("http://localhost:8082/tomcats/test", requestEntity, Boolean.class);

        System.out.println(connect);
    }

    @Test
    public void tests() {
        TomcatNodeManager tomcatNodeManager = new TomcatNodeManager("192.168.1.6", "7777", null, null, null);
        TomcatNodeMetric metric = tomcatNodeManager.getNodeMetric();
        HostDynamicMetric sds = tomcatNodeManager.getHostDynamicMetric();
        List<ProtocolDynamicMetric> list = tomcatNodeManager.getProtocolDynamicMetric();
        List<ThreadPoolDynamicMetric> list1 = tomcatNodeManager.getThreadPoolDynamicMetric();
        System.out.println(1);
    }

    @Test
    public void test2() {
        TomcatManager tomcatManager = new TomcatManager("192.168.1.6:7777,192.168.1.6:8888");
        System.out.println(tomcatManager.testTomcatConnect());
    }

    @Test
    public void testReflect() {
        try {
            TomcatNodeManager tomcatNodeManager = new TomcatNodeManager("192.168.1.6", "7777", null, null, null);
            TomcatJmx jmx = new TomcatJmx("192.168.1.6", "7777");
            jmx.connect();
            MBeanServerConnection mbsc = jmx.getConnection();
            ObjectName objName = new ObjectName(TomcatMetrics.getEngineName(mbsc) + ":type=Manager,context=*,host=*");
            Set<ObjectName> set = mbsc.queryNames(objName, null);
            ObjectName hostDynamicObjName = set.iterator().next();
            AttributeList attributes = mbsc.getAttributes(hostDynamicObjName, MetricArray.HOST_DYNAMIC_METRIC);
            HostDynamicMetric hostDynamicMetric = new HostDynamicMetric();
            for (Attribute attribute : attributes.asList()) {
                String name = attribute.getName();
                Object value = attribute.getValue();
                Field field = hostDynamicMetric.getClass().getDeclaredField(name);
                field.setAccessible(true);
                field.set(hostDynamicMetric, value);
            }
            System.out.println(111);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testPage() {
        List<String> list = Arrays.asList("1", "2", "3", "4");
        PageImpl<String> page = listConvertToPage(list, new PageRequest(0, 3));
        System.out.println(page);

    }

    private <T> PageImpl<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    @Test
    public void testRegix() {
        String sql = "sel ect into xxx 'sada' up date alte";
        Pattern p = Pattern.compile(".*(update|UPDATE|INSERT|insert|delete|DELETE|create|CREATE|DROP|drop|alter|ALTER).*");
        Matcher m = p.matcher(sql);
        if (m.matches()) {
            System.out.println(false);
        } else {
            System.out.println(true);
        }
    }

    @Test
    public void testStringUtils() {
        String ss = new String();
        System.out.println(StringUtils.isEmpty(ss));
    }

    @Test
    public void testFutureTask() {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService= Executors.newSingleThreadExecutor();
        FutureTask<Long> futureTask = new FutureTask<>(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Thread.sleep(1000);
                return System.currentTimeMillis();
            }
        });
        executorService.execute(futureTask);
        try {
            long endTime = futureTask.get(9, TimeUnit.SECONDS);
            System.out.println("time:" + (endTime - startTime));
        } catch (Exception e) {
            System.out.println("time:9000L");
        }
    }

    @Test
    public  void testBigDecimal() {
        BigDecimal bigDecimal = new BigDecimal(2);
        BigDecimal bDouble = new BigDecimal(2.1);
        BigDecimal bString = new BigDecimal("2.3");
        System.out.println("bigDecimal=" + bigDecimal);
        System.out.println("bDouble=" + bDouble);
        System.out.println("bString=" + bString);
        System.out.println(bString.compareTo(new BigDecimal(2.4)));

        double ss = 1d/6d;
        System.out.println(ss);
    }

    @Test
    public void testEnum(){
        System.out.println(getFrequencyPeriod(AcquisitionFrequency.TEN_SECOND));
    }
    private double getFrequencyPeriod(AcquisitionFrequency acquisitionFrequency) {
        String frequencyName =  acquisitionFrequency.name();
        switch (frequencyName){
            case "ONE_MINUTE":
                return 1d;
            case "FIVE_MINUTE":
                return 5d;
            case "TEN_MINUTE":
                return 10d;
            case "FIFTEEN_MINUTE":
                return 15d;
            case "HALF_HOUR":
                return 30d;
            case "ONE_HOUR":
                return 60d;
            case "ONE_DAY":
                return 1440d;
            case "HALF_MINUTE":
                return 0.5d;
            default:
                return 1d / 6d;

        }
    }


}
