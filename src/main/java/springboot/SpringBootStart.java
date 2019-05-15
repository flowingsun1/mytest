package springboot;

import com.lyy.tomcat.test.Test;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lyy
 * @Date: 2019/2/18 10:27
 */
@SpringBootApplication
@RestController
public class SpringBootStart {
    private static final String CHECK_TOMCAT_CONNECT = "/tomcats/test/{jmxConnect}";
    private static Logger logger = LoggerFactory.getLogger(Test.class);
    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @RequestMapping("/api")
    public Boolean api() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> param = new HashMap<>(1);
        param.put("jmxConnect", "localhost:8888,localhost:8889");
        ResponseEntity<Boolean> exchange = restTemplate.exchange("http://localhost:9081" + CHECK_TOMCAT_CONNECT, HttpMethod.GET, null, Boolean.class, param);
        return exchange.getBody();
    }


    public static void main(String[] args) {

        SpringApplication.run(SpringBootStart.class,args);
        logger.debug("lueluelue");
    }


}
