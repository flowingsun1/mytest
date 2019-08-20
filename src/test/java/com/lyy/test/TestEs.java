package com.lyy.test;

import com.google.gson.Gson;
import com.transwarp.nxms.elasticsearch.annotation.EnableNxmsElasticsearchClient;
import com.transwarp.nxms.elasticsearch.config.TranswarpElasticsearchProperties;
import com.transwarp.nxms.elasticsearch.service.NxmsElasticsearchService;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: lyy
 * @Date: 2019/8/5 17:02
 */
public class TestEs {

    @Test
    public void sss(){
        TranswarpElasticsearchProperties properties = new TranswarpElasticsearchProperties();
        properties.setHost(new String[]{"192.168.1.11","192.168.1.12","192.168.1.13"});
        properties.setPort(9201);
    }
    @Test
    public void lambda(){
        List<String> list = Arrays.asList("a1","a2","a3");
        list.stream().filter(t -> t.contains("a")).forEach(System.out::println);
    }
}
