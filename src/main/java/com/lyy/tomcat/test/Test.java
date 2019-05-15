package com.lyy.tomcat.test;

import com.lyy.tomcat.manager.TomcatManager;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author: lyy
 * @Date: 2019/5/13 15:31
 */
@Slf4j
public class Test {
    public static void main(String[] args) {
        TomcatManager tomcatManager = new TomcatManager("localhost:8888,localhost:8889");
        boolean connect = tomcatManager.testTomcatConnect();
        System.out.println(connect);
        log.info(String.valueOf(connect));
        log.debug("hahha");
    }
}
