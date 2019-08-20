package springboot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: lyy
 * @Date: 2019/7/5 16:07
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "lyy.kafka")
public class KafkaProperties {
    private String url;
    private String userName;
    private String password;
}
