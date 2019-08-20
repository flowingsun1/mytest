package com.transwarp.nxms.elasticsearch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author ZGW
 * @Date 2019/3/14
 */
@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "transwarp.elasticsearch.server")
public class TranswarpElasticsearchProperties {
    private String[] host;
    private int port = 9200;
}
