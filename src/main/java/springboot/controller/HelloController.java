package springboot.controller;

import com.transwarp.nxms.elasticsearch.service.NxmsElasticsearchClient;
import com.transwarp.nxms.elasticsearch.service.NxmsElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springboot.properties.Config;
import springboot.properties.KafkaProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lyy
 * @Date: 2019/7/5 16:01
 */
@RestController
@Configuration
@EnableConfigurationProperties({Config.class, KafkaProperties.class})
public class HelloController {
    @Autowired
    private Config config;
    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private NxmsElasticsearchClient nxmsElasticsearchClient;

    private static final String CHECK_TOMCAT_CONNECT = "/tomcats/test/{jmxConnect}";
    @RequestMapping("/hello")
    public String hello() {
        return config.getAge()+config.getName() + "---"+kafkaProperties.getUserName()+kafkaProperties.getPassword()+kafkaProperties.getUrl();
    }
    @RequestMapping("/es")
    public boolean es() {
        return nxmsElasticsearchClient.test();
    }

    @RequestMapping("/api")
    public Boolean api() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> param = new HashMap<>(1);
        param.put("jmxConnect", "localhost:8888,localhost:8999");
        ResponseEntity<Boolean> exchange = restTemplate.exchange("http://192.168.1.95:9082" + CHECK_TOMCAT_CONNECT, HttpMethod.GET, null, Boolean.class, param);
        return exchange.getBody();
    }
}
