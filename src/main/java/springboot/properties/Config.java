package springboot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: lyy
 * @Date: 2019/7/5 15:48
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "lyy.test")
public class Config {
    private String age;
    private String name;
}
