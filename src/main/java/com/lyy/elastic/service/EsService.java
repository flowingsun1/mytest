package com.lyy.elastic.service;

import com.transwarp.nxms.elasticsearch.service.NxmsElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lyy
 * @Date: 2019/8/5 17:01
 */
@Component
public class EsService {

    @Autowired
    private NxmsElasticsearchService nxmsElasticsearchService;
}
