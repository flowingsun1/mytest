package com.lyy.tomcat.jmx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.naming.Context;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyy
 * @date 2018/12/21.
 * tomcat jmx连接,只需传入ip:port即可
 */
public class TomcatJmx {

    private Log log = LogFactory.getLog(TomcatJmx.class);

    private static final String TOMCAT_JMX = "service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi";

    private static final Map<String, Object> defaultJmxConnectorProperties;

    static {
        defaultJmxConnectorProperties = new HashMap<>();
        defaultJmxConnectorProperties.put("jmx.remote.x.request.waiting.timeout", 3000L);
        defaultJmxConnectorProperties.put("jmx.remote.x.notification.fetch.timeout", 3000L);
        defaultJmxConnectorProperties.put("sun.rmi.transport.connectionTimeout", 3000L);
        defaultJmxConnectorProperties.put("sun.rmi.transport.tcp.handshakeTimeout", 3000L);
        defaultJmxConnectorProperties.put("sun.rmi.transport.tcp.responseTimeout", 3000L);
    }

    private String host;

    private String jmxPort;

    private String jmxUser = null;

    private String jmxPass = null;

    private Boolean jmxSsl = false;

    private MBeanServerConnection connection;

    private JMXConnector connector;

    public TomcatJmx(String host, String jmxPort) {
        this(host, jmxPort, null, null, false);
    }

    public TomcatJmx(String host, String jmxPort, String jmxUser, String jmxPass, Boolean jmxSsl) {
        this.host = host;
        this.jmxPort = jmxPort;
        this.jmxUser = jmxUser;
        this.jmxPass = jmxPass;
        this.jmxSsl = jmxSsl;
    }

    public void connect() {
        try {
            if (Integer.valueOf(jmxPort) <= 0){
                return;
            }
            String[] credentials = new String[]{jmxUser, jmxPass};
            defaultJmxConnectorProperties.put(JMXConnector.CREDENTIALS, credentials);
            if (jmxSsl) {
                SslRMIClientSocketFactory clientSocketFactory = new SslRMIClientSocketFactory();
                defaultJmxConnectorProperties.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, clientSocketFactory);
                defaultJmxConnectorProperties.put(Context.SECURITY_PROTOCOL, "ssl");
                defaultJmxConnectorProperties.put("com.sun.jndi.rmi.factory.socket", clientSocketFactory);
            }

            JMXServiceURL jmxServiceURL = new JMXServiceURL(String.format(TOMCAT_JMX, host, jmxPort));
            connector = JMXConnectorFactory.connect(jmxServiceURL, defaultJmxConnectorProperties);
            connection = connector.getMBeanServerConnection();
        } catch (IOException e) {
            log.error(host + " cant be connected");
        }
    }



    public void close() {
        if (connector != null) {
            try {
                connector.close();
            } catch (IOException e) {
                log.warn("jmx connect close error,cause by " + e.getMessage());
            }
        }
    }

    public MBeanServerConnection getConnection() {
        return connection;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setJmxPort(String jmxPort) {
        this.jmxPort = jmxPort;
    }

    public void setJmxUser(String jmxUser) {
        this.jmxUser = jmxUser;
    }

    public void setJmxPass(String jmxPass) {
        this.jmxPass = jmxPass;
    }

    public void setJmxSsl(Boolean jmxSsl) {
        this.jmxSsl = jmxSsl;
    }

    public JMXConnector getConnector() {
        return connector;
    }

    public void setConnector(JMXConnector connector) {
        this.connector = connector;
    }
}
