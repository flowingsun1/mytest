package com.transwarp.nxms.elasticsearch.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ZGW
 * @Date 2019/3/13
 */
@Slf4j
public class NxmsElasticsearchService {

    private NxmsElasticsearchClient nxmsElasticsearchClient;

    public NxmsElasticsearchService(NxmsElasticsearchClient nxmsElasticsearchClient) {
        this.nxmsElasticsearchClient = nxmsElasticsearchClient;
    }

    /**
     * 操作ES索引，检测索引、别名是否存在，不存在则创建索引或者别名，
     *
     * @param metric     索引
     * @param timeString 日期格式 如yyyy-MM-dd,yyyy-MM
     */
    public void operationIndex(String metric, String timeString) {
        String index = metric + "-" + LocalDate.now().toString(timeString);
        //检查索引是否存在
        boolean isExist = nxmsElasticsearchClient.checkIndexExist(index);
        boolean aliasExist;
        if (isExist) {
            //检查别名是否存在
            aliasExist = nxmsElasticsearchClient.checkAliasExist(index, metric);
            if (!aliasExist) {
                //创建索引别名
                nxmsElasticsearchClient.createAlias(index, metric);
            }
        } else {
            //创建索引和别名
            nxmsElasticsearchClient.createIndexAndAlias(index, metric);
        }
    }

    /**
     * 操作es索引，判断该索引、映射、别名是否存在，不存在则创建
     *
     * @param alias       索引别名
     * @param timeString  日期模板格式
     * @param mappingType 映射类型
     * @param fileName    映射文件名
     * @param shard       主分片数
     * @param replicas    副本数
     */
    public void operationIndex(String alias, String timeString, String mappingType, String fileName, int shard, int replicas) {
        try {
            String index = alias + "-" + LocalDate.now().toString(timeString);
            boolean exist = nxmsElasticsearchClient.checkIndexExist(index);
            if (!exist) {
                Map<String, Object> setting = new HashMap<>(3);
                setting.put("number_of_shards", shard);
                setting.put("number_of_replicas", replicas);
                nxmsElasticsearchClient.createIndexAndMappingAndAlias(index, alias, mappingType, fileName, setting);
            } else {
                boolean mappingExist = nxmsElasticsearchClient.checkIndexMappingExist(index, mappingType);
                if (!mappingExist) {
                    nxmsElasticsearchClient.createMapping(index, mappingType, fileName);
                }
                boolean aliasExist = nxmsElasticsearchClient.checkAliasExist(index, alias);
                if (!aliasExist) {
                    nxmsElasticsearchClient.createAlias(index, alias);
                }
            }
        } catch (Exception e) {
            log.error("operationIndex failed:" + e.getMessage());
        }

    }

    /**
     * 初始化模板映射，创建或者更新template
     *
     * @param templateName 模板名称
     * @param fileName     文件名称
     */
    public void initTemplateMapping(String templateName, String fileName) {
        nxmsElasticsearchClient.createOrUpdateTemplateMapping(templateName, fileName);
    }

    public String searchData(String target, String from, String to, AggregationBuilder aggregationBuilder, DateHistogramInterval dateHistogramInterval, PipelineAggregationBuilder pipelineAggregationBuilder, long minDocCount) {
        SearchRequest searchRequest = nxmsElasticsearchClient.buildSearchRequest(target, from, to, aggregationBuilder, dateHistogramInterval, pipelineAggregationBuilder, minDocCount);
        return nxmsElasticsearchClient.searchMetric(searchRequest);
    }
}