package com.transwarp.nxms.elasticsearch.annotation;

import com.transwarp.nxms.elasticsearch.config.KerberosProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;

/**
 * @author dzg
 * @date 2019/4/3
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@EnableConfigurationProperties({KerberosProperties.class})
public @interface EnableKerberos {
}
