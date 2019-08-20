package com.transwarp.nxms.elasticsearch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dzg
 * @date 2019/4/3
 */
@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "nxms.kerberos")
public class KerberosProperties {
    public static final String SECURITY_PROTOCOL = "security.protocol";
    public static final String SASL_KERBEROS_SERVICE_PRINCIPAL_INSTANCE = "sasl.kerberos.service.principal.instance";
    public static final String SASL_KERBEROS_SERVICE_NAME = "sasl.kerberos.service.name";
    public static final String SASL_JAAS_CONFIG = "sasl.jaas.config";
    public static final String JAVA_LOGIN_CONFIG_PARAM = "java.security.auth.login.config";
    /**
     * security.protocol
     */
    private String securityProtocol = "PLAINTEXT";
    /**
     * sasl.kerberos.service.principal.instance
     */
    private String saslKerberosServicePrincipalInstance = "";
    /**
     * sasl.kerberos.service.name
     */
    private String saslKerberosServiceName = "";


    public void kerberosConfigMap(Map<String, Object> configs) {
        configs.put(SECURITY_PROTOCOL, securityProtocol);
        configs.put(SASL_KERBEROS_SERVICE_PRINCIPAL_INSTANCE, saslKerberosServicePrincipalInstance);
        configs.put(SASL_KERBEROS_SERVICE_NAME, saslKerberosServiceName);
    }
}
