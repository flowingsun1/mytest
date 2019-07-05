package com.lyy.test;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: lyy
 * @Date: 2019/5/17 16:41
 */
public class RegexTest {
    @Test
    public void test(){
        String str = "localhost:8888,192.168.1.32:8999";
        String regex = "(?:([,\\,\\,]))+";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        while(m.find()){
            System.out.println(m.group());
        }
    }
}
