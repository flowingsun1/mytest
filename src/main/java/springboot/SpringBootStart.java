package springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lyy
 * @Date: 2019/2/18 10:27
 */
@SpringBootApplication
@RestController
public class SpringBootStart {
    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }


    public static void main(String[] args) {

        SpringApplication.run(SpringBootStart.class,args);
    }


}
