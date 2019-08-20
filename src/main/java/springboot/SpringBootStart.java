package springboot;

import com.transwarp.nxms.elasticsearch.annotation.EnableNxmsElasticsearchClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author: lyy
 * @Date: 2019/2/18 10:27
 */
@SpringBootApplication
@EnableNxmsElasticsearchClient
public class SpringBootStart {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootStart.class,args);
    }


}
