package com.transwarp.nxms.elasticsearch.domain.metrics.other;

import com.transwarp.nxms.elasticsearch.domain.metrics.other.fs.Fs;
import com.transwarp.nxms.elasticsearch.domain.metrics.other.process.Process;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/9 16:50
 */
@Getter
public class Others {
    private Process process;
    private Fs fs;
    private Transport transport;
    private Http http;
    private Script script;
}
