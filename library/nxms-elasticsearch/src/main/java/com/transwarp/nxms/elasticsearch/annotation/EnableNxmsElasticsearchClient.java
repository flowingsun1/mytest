package com.transwarp.nxms.elasticsearch.annotation;

import com.transwarp.nxms.elasticsearch.config.ElasticsearchConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author ZGW
 * @Date 2019/3/14
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ElasticsearchConfig.class)
public @interface EnableNxmsElasticsearchClient {
}
