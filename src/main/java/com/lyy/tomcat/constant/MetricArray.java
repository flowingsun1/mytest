package com.lyy.tomcat.constant;

class MetricArray {

    static final String []CLUSTER_STR = new String[]{"clusterName","stateName","channelSendOptions",
            "channelStartOptions","heartbeatBackgroundEnabled","notifyLifecycleListenerOnFailure"};

    static final String []SERVER_STR = new String[]{"address","modelerType","port","serverBuilt",
            "serverInfo","shutdown","stateName"};

    static final String []ENGINE_STR = new String[]{"backgroundProcessorDelay","catalinaBase",
            "defaultHost","jvmRoute","modelerType","name","startChildren","startStopThreads","stateName"};

    static final String [] HOST_STR = new String[]{"appBase","autoDeploy","backgroundProcessorDelay",
            "configClass","contextClass","copyXML","createDirs","deployOnStartup","deployXML","name",
            "startChildren","startStopThreads","stateName","undeployOldVersions","unpackWARs"};

    static final String [] AJP_STR = new String[]{"acceptCount","acceptorThreadCount","acceptorThreadPriority",
            "ajpFlush","aprRequired","port","maxConnections","maxHeaderCount","maxThreads","minSpareThreads",
            "modelerType","name","tcpNoDelay","threadPriority","tomcatAuthentication","tomcatAuthorization"};

    static final String [] HTTP_STR = new String[]{"acceptCount","acceptorThreadCount","acceptorThreadPriority",
            "algorithm","allowHostHeaderMismatch","port","maxConnections","maxHeaderCount",
            "maxHttpHeaderSize","maxThreads","minSpareThreads","modelerType","name","tcpNoDelay",
            "maxExtensionSize","rejectIllegalHeaderName"};

    static final String [] THREADPOOL_STR = new String[]{"alpnSupported","currentThreadCount",
            "currentThreadsBusy", "daemon","executorTerminationTimeoutMillis","modelerType","port",
           "running","pollerThreadCount","pollerThreadPriority"};

    static final String [] SERVLET_STR = new String[]{"asyncSupported","classLoadTime","countAllocated",
            "errorCount","maxInstances","maxTime","minTime","processingTime","requestCount","stateName"};


    static final String [] HOST_DYNAMIC_METRIC = new String[]{"activeSessions","counterNoStateTransfered",
            "expiredSessions","maxActive","maxActiveSessions","rejectedSessions",
            "sessionAverageAliveTime","sessionMaxAliveTime","sessionReplaceCounter"};

    static final String [] PROTOCOL_DYNAMIC_METRIC  = new String[]{"bytesReceived","bytesSent","errorCount",
            "processingTime", "requestCount"};

    static final String [] THREADPOOL_DYNAMIC_METRIC  = new String[]{"connectionCount","acceptorThreadCount",
            "currentThreadCount","currentThreadsBusy"};




}
