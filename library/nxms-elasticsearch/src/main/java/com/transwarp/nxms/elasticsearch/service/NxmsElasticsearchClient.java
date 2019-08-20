package com.transwarp.nxms.elasticsearch.service;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.transwarp.nxms.elasticsearch.domain.ActionType;
import com.transwarp.nxms.elasticsearch.domain.AliasesRequest;
import com.transwarp.nxms.elasticsearch.domain.mapping.MappingMetaData;
import com.transwarp.nxms.elasticsearch.util.LoadResourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.template.get.GetIndexTemplatesRequest;
import org.elasticsearch.action.admin.indices.template.get.GetIndexTemplatesResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.action.search.RestSearchAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * @Author ZGW
 * @Date 2019/3/13
 */
@Slf4j
public class NxmsElasticsearchClient {

    private final RestHighLevelClient restHighLevelClient;

    private final Gson gson;

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm");


    public NxmsElasticsearchClient(RestHighLevelClient restHighLevelClient, Gson gson) {
        this.restHighLevelClient = restHighLevelClient;
        this.gson = gson;
    }

    private static long converToLong(String value) {
        LocalDateTime dateFrom = LocalDateTime.parse(value, FORMATTER);
        return dateFrom.toDate().getTime();
    }

    private SearchSourceBuilder buildSearchSourceBuilder(String target, long start, long end) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(0);
        RangeQueryBuilder rangbuilder = QueryBuilders.rangeQuery("timestamp");
        rangbuilder.gte(start).lte(end);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("metric", target)).filter(rangbuilder);
        searchSourceBuilder.query(queryBuilder);
        return searchSourceBuilder;
    }

    private DateHistogramAggregationBuilder buildAggregation(DateHistogramInterval dateHistogramInterval, AggregationBuilder aggregationBuilder, PipelineAggregationBuilder pipelineAggregationBuilder, long minDocCount) {
        DateHistogramAggregationBuilder interval = new DateHistogramAggregationBuilder("dps") {
            @Override
            protected XContentBuilder doXContentBody(XContentBuilder builder, Params params) throws IOException {
                builder.field(Histogram.INTERVAL_FIELD.getPreferredName(), this.dateHistogramInterval().toString());
                builder.field(Histogram.MIN_DOC_COUNT_FIELD.getPreferredName(), this.minDocCount());
                return builder;

            }
        };
        interval.field("timestamp").dateHistogramInterval(dateHistogramInterval).minDocCount(minDocCount);
        interval.subAggregation(aggregationBuilder);
        if (pipelineAggregationBuilder != null) {
            interval.subAggregation(pipelineAggregationBuilder);
        }
        return interval;
    }


    /**
     * 构建查询
     *
     * @param target                     索引
     * @param from                       开始时间
     * @param to                         结束时间
     * @param aggregationBuilder         聚合类型
     * @param dateHistogramInterval      聚合间隔
     * @param pipelineAggregationBuilder 管道聚合
     * @param minDocCount                存在的最小文本数
     * @return
     */
    public SearchRequest buildSearchRequest(String target, String from, String to, AggregationBuilder aggregationBuilder, DateHistogramInterval dateHistogramInterval, PipelineAggregationBuilder pipelineAggregationBuilder, long minDocCount) {
        long start = converToLong(from);
        long end = converToLong(to);
        SearchRequest searchRequest = new SearchRequest("metric.store");
        searchRequest.routing(target);
        SearchSourceBuilder searchSourceBuilder = buildSearchSourceBuilder(target, start, end);
        DateHistogramAggregationBuilder interval = buildAggregation(dateHistogramInterval, aggregationBuilder, pipelineAggregationBuilder, minDocCount);
        searchSourceBuilder.aggregation(interval);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    /**
     * 通过searchRequest获取查询结果
     * 使用restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT)方法查询，低版本es解析返回数据异常不兼容、
     * 所以将SearchRequest转化为Request参数，使用restHighLevelClient.getLowLevelClient().performRequest(request)
     * 返回Response处理
     *
     * @param searchRequest
     * @return
     */
    public String searchMetric(SearchRequest searchRequest) {
        try {
            ActionRequestValidationException validationException = searchRequest.validate();
            if (validationException != null) {
                throw validationException;
            }
            Request request = search(searchRequest);
            request.setOptions(RequestOptions.DEFAULT);
            Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
            return EntityUtils.toString(response.getEntity(), "utf8");
        } catch (Exception e) {
            log.error("Failed to retrieve historical query data:" + e.getMessage());
        }
        return null;
    }

    static String endpoint(String[] indices, String endpoint) {
        return "/" + indices[0] + "/" + endpoint;
    }

    private static Request search(SearchRequest searchRequest) throws IOException {
        Request request = new Request(HttpPost.METHOD_NAME, endpoint(searchRequest.indices(), "_search"));
        Params params = new Params(request);
        addSearchRequestParams(params, searchRequest);
        if (searchRequest.source() != null) {
            BytesRef source = XContentHelper.toXContent(searchRequest.source(), XContentType.JSON, false).toBytesRef();
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(source.bytes, source.offset, source.length, ContentType.create(XContentType.JSON.mediaTypeWithoutParameters(), (Charset) null));
            request.setEntity(byteArrayEntity);
        }
        return request;
    }

    private static void addSearchRequestParams(Params params, SearchRequest searchRequest) {
        params.putParam(RestSearchAction.TYPED_KEYS_PARAM, "true");
        params.withRouting(searchRequest.routing());
        params.withPreference(searchRequest.preference());
        params.withIndicesOptions(searchRequest.indicesOptions());
        params.putParam("search_type", searchRequest.searchType().name().toLowerCase(Locale.ROOT));
        if (searchRequest.requestCache() != null) {
            params.putParam("request_cache", Boolean.toString(searchRequest.requestCache()));
        }
        if (searchRequest.allowPartialSearchResults() != null) {
            params.putParam("allow_partial_search_results", Boolean.toString(searchRequest.allowPartialSearchResults()));
        }
        params.putParam("batched_reduce_size", Integer.toString(searchRequest.getBatchedReduceSize()));
        if (searchRequest.scroll() != null) {
            params.putParam("scroll", searchRequest.scroll().keepAlive());
        }
    }

    /**
     * @param index 索引
     * @return true is exist else not exist
     * @description 检查索引是否存在
     */
    public boolean checkIndexExist(String index) {
        try {
            Response response = restHighLevelClient.getLowLevelClient().performRequest(new Request("HEAD", index));
            return response.getStatusLine().getStatusCode() == 200;
        } catch (Exception e) {
            log.error("checkIndexExist failed:" + e.getMessage());
        }
        return false;
    }

    /**
     * @param index 索引
     * @param type  mapping类型
     * @return
     * @description 检查mapping是否存在
     */
    public boolean checkIndexMappingExist(String index, String type) {
        try {
            Request request = new Request("get", index + "/_mapping");
            Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            JsonObject jsonObject = gson.fromJson(new InputStreamReader(content), JsonObject.class);
            JsonObject indexJson = jsonObject.getAsJsonObject(index);
            JsonObject mappingsJson = indexJson.getAsJsonObject("mappings");
            JsonObject typeJson = mappingsJson.getAsJsonObject(type);
            if (typeJson != null) {
                return true;
            }
        } catch (Exception e) {
            log.error("checkMappingExist failed:" + e.getMessage());
        }
        return false;
    }

    /**
     * @param templateName
     * @param fileName
     */
    public void createOrUpdateTemplateMapping(String templateName, String fileName) {
        try {
            String path = getFilePath(fileName);
            Request request = new Request("put", "_template/" + templateName);
            JsonObject jsonObject = LoadResourceUtils.getJsonObject(gson, path);
            HttpEntity entity = new NStringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            restHighLevelClient.getLowLevelClient().performRequest(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * @param index 索引
     * @param alias 别名
     * @return
     * @description 检查别名是否存在
     */
    public boolean checkAliasExist(String index, String alias) {
        try {
            IndicesClient indices = restHighLevelClient.indices();
            GetAliasesRequest getAliasesRequest = new GetAliasesRequest();
            getAliasesRequest.aliases(alias);
            GetAliasesResponse aliasesResponse = indices.getAlias(getAliasesRequest, RequestOptions.DEFAULT);
            Map<String, Set<AliasMetaData>> aliases = aliasesResponse.getAliases();
            Set<AliasMetaData> aliasMetaData = aliases.get(index);
            if (aliasMetaData != null) {
                return true;
            }
        } catch (Exception e) {
            log.error("checkAliasExist failed:" + e.getMessage());
        }
        return false;
    }

    /**
     * @param index 索引
     * @param alias 别名
     * @description 创建别名
     */
    public void createAlias(String index, String alias) {
        try {
            AliasesRequest aliasesRequest = new AliasesRequest();
            aliasesRequest.addActions(index, alias, ActionType.ADD);
            Request request = new Request("post", "_aliases");
            request.setJsonEntity(gson.toJson(aliasesRequest));
            restHighLevelClient.getLowLevelClient().performRequest(request);
        } catch (Exception e) {
            log.error("create alias failed:" + e.getMessage());
        }
    }

    /**
     * @param index
     * @param alias
     * @description 移除索引别名
     */
    public void removeAlias(String index, String alias) {
        try {
            AliasesRequest aliasesRequest = new AliasesRequest();
            aliasesRequest.addActions(index, alias, ActionType.REMOVE);
            Request request = new Request("post", "_aliases");
            request.setJsonEntity(gson.toJson(aliasesRequest));
            restHighLevelClient.getLowLevelClient().performRequest(request);
        } catch (Exception e) {
            log.error("remove alias failed:" + e.getMessage());
        }
    }

    /**
     * @param fileName
     * @return
     * @throws IOException
     * @description 通过检测es版本获取mapping文件目录
     */
    public String getFilePath(String fileName) throws IOException {
        float elasticsearchVersion = getElasticsearchVersion();
        String path;
        if (elasticsearchVersion >= 5) {
            path = "mapping/es-v5-upper/" + fileName + ".json";
        } else {
            path = "mapping/es-v5-lower/" + fileName + ".json";
        }
        return path;
    }

    public float getElasticsearchVersion() throws IOException {
        Request request = new Request("get", "");
        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        HttpEntity entity = response.getEntity();
        Map objectMap = gson.fromJson(new InputStreamReader(entity.getContent()), Map.class);
        Map versionObject = (Map) objectMap.get("version");
        String number = versionObject.get("number").toString();
        String[] strings = number.split("\\.");
        String string = strings[0] + "." + strings[1];
        return Float.valueOf(string);
    }

    /**
     * @param templateName
     * @return
     * @description 检测模板是否存储
     */
    public boolean checkTemplateMappingExit(String templateName) {
        try {
            GetIndexTemplatesRequest getIndexTemplatesRequest = new GetIndexTemplatesRequest();
            getIndexTemplatesRequest.names(templateName);
            GetIndexTemplatesResponse template = restHighLevelClient.indices().getTemplate(getIndexTemplatesRequest, RequestOptions.DEFAULT);
            return !template.getIndexTemplates().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * @param index    索引
     * @param alias    别名
     * @param type     mapping类型
     * @param fileName mapping文件名
     * @description 创建索引映射和别名
     */
    public void createIndexAndMappingAndAlias(String index, String alias, String type, String fileName, Map setting) {
        try {
            String filePath = getFilePath(fileName);
            JsonObject jsonProperties = LoadResourceUtils.getJsonObject(gson, filePath);
            if (jsonProperties != null) {
                IndicesClient indices = restHighLevelClient.indices();
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
                createIndexRequest.alias(new Alias(alias));
                createIndexRequest.settings(setting);
                createIndexRequest.mapping(type, jsonProperties.toString(), XContentType.JSON);
                indices.create(createIndexRequest, RequestOptions.DEFAULT);
            }
        } catch (Exception e) {
            log.error("createIndexAndMappingAndAlias" + e.getMessage());
        }
    }

    /**
     * @param index 索引
     * @param alias 别名
     * @descriptionn 创建索引别名
     */
    public void createIndexAndAlias(String index, String alias) {
        try {
            IndicesClient indices = restHighLevelClient.indices();
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
            createIndexRequest.alias(new Alias(alias));
            indices.create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * @param index    索引
     * @param type     映射类型
     * @param fileName mapping文件名
     * @description 创建mapping
     */
    public void createMapping(String index, String type, String fileName) {
        try {
            String filePath = getFilePath(fileName);
            JsonObject jsonProperties = LoadResourceUtils.getJsonObject(gson, filePath);
            if (jsonProperties != null) {
                JsonObject jsonType = new JsonObject();
                jsonType.add(type, jsonProperties);
                JsonObject jsonMappings = new JsonObject();
                jsonMappings.add("mappings", jsonType);
                HttpEntity entity = new NStringEntity(jsonMappings.toString(), ContentType.APPLICATION_JSON);
                Request request = new Request("put", index);
                request.setEntity(entity);
                restHighLevelClient.getLowLevelClient().performRequest(request);
            }
        } catch (Exception e) {
            log.error("createMapping failed:" + e.getMessage());
        }
    }

    /**
     * 关闭索引
     *
     * @param index 索引
     */
    public void closeIndex(String index) {
        try {
            //关闭索引
            Request closeRequest = new Request("post", index + "/_close");
            restHighLevelClient.getLowLevelClient().performRequest(closeRequest);
        } catch (Exception e) {
            log.error("close index failed:" + e.getMessage());
        }
    }

    /**
     * @param index
     */
    public void deleteIndex(String index) {
        try {
            //删除索引
            Request request = new Request("delete", index);
            restHighLevelClient.getLowLevelClient().performRequest(request);
        } catch (Exception e) {
            log.error("delete index failed:" + e.getMessage());
        }
    }

    /**
     * 获取文档数量
     *
     * @param index  索引
     * @param target 目标
     * @return
     */
    public Long getDocumentNumber(String index, String target) {
        boolean exist = checkIndexExist(index);
        long documentNumber = 0L;
        if (exist) {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(index);
            if (target != null) {
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                searchSourceBuilder.query(termQuery("metric", target));
                searchRequest.source(searchSourceBuilder);
            }
            try {
                SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                documentNumber = search.getHits().getTotalHits();
            } catch (Exception e) {
                log.error("failed to get the number of logs today :" + e.getMessage());
            }
        }
        return documentNumber;
    }

    /**
     * 查询文档
     *
     * @param index 索引
     * @param body  查询主体
     * @return
     * @throws IOException
     */
    public String searchIndex(String index, String body) throws IOException {
        Request request = new Request("post", String.format("%s/_search", index));
        request.setJsonEntity(body);
        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        InputStream content = response.getEntity().getContent();
        JsonObject jsonObject = gson.fromJson(new InputStreamReader(content, Charset.forName("utf-8")), JsonObject.class);
        return jsonObject.toString();
    }

    /**
     * 获取inceptor sql 监控数据
     *
     * @param index  索引
     * @param type   类型
     * @param status 状态
     * @return
     * @throws IOException
     */
    public SearchHits getInceptorSqls(String index, String type, String status, int offset, int pageSize) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.types(type);
        FieldSortBuilder fieldSortBuilder = new FieldSortBuilder("id");
        fieldSortBuilder.order(SortOrder.DESC);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().from(offset).size(pageSize);
        searchSourceBuilder.sort(fieldSortBuilder);
        if ("*".equals(status)) {
            BoolQueryBuilder boolQueryBuilder = boolQuery();
            boolQueryBuilder.should(termQuery("status", "failed"));
            boolQueryBuilder.should(termQuery("status", "completed"));
            searchSourceBuilder.query(boolQueryBuilder);
        } else {
            searchSourceBuilder.query(termQuery("status", status));
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return search.getHits();
    }

    /**
     * 获取索引映射
     *
     * @param index 索引
     * @return
     */
    public Map<String, MappingMetaData> getIndexMappings(String index) throws IOException {
        Map<String, MappingMetaData> mappingMetaDataMap = new HashMap<>();
        Response get = restHighLevelClient.getLowLevelClient().performRequest(new Request("get", String.format("%s/_mappings", index)));
        InputStream content = get.getEntity().getContent();
        JsonObject jsonObject = gson.fromJson(new InputStreamReader(content), JsonObject.class);
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> next : entries) {
            JsonElement value = next.getValue();
            MappingMetaData mappingMetaData = gson.fromJson(value, MappingMetaData.class);
            mappingMetaDataMap.put(next.getKey(), mappingMetaData);
        }
        return mappingMetaDataMap;
    }

    /**
     * 判断es中是否存在该索引，不存在则创建索引以及该索引的映射
     *
     * @param index    索引名称
     * @param fileName 索引映射文件名
     */
    public void initIndex(String index, String fileName) {
        try {
            boolean exist = checkIndexExist(index);
            if (!exist) {
                String filePath = getFilePath(fileName);
                InputStream inputStream = LoadResourceUtils.getReader(filePath);
                HttpEntity entity = new InputStreamEntity(inputStream, ContentType.APPLICATION_OCTET_STREAM);
                Request request = new Request("put", index);
                request.setEntity(entity);
                restHighLevelClient.getLowLevelClient().performRequest(request);
            }
        } catch (Exception e) {
            log.error("init elasticsearch index failed:" + e.getMessage());
        }
    }

    /**
     * 初始化索引
     *
     * @param index
     */
    public void initIndex(String index) {
        boolean exist = checkIndexExist(index);
        if (!exist) {
            Map<String, Object> setting = new HashMap<>(3);
            setting.put("number_of_shards", 3);
            setting.put("number_of_replicas", 1);
            try {
                IndicesClient indices = restHighLevelClient.indices();
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
                createIndexRequest.settings(setting);
                indices.create(createIndexRequest, RequestOptions.DEFAULT);
            } catch (Exception e) {
                log.error("create index error:" + e.getMessage());
            }
        }
    }

    /**
     * 检索日志内容
     *
     * @param searchRequest
     * @return
     */
    public List<String> searchLog(SearchRequest searchRequest) {
        List<String> list = new ArrayList<>();
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            for (SearchHit searchHit : hits) {
                String message = searchHit.getSourceAsMap().get("message").toString();
                list.add(message);
            }
        } catch (Exception e) {
            log.error("search log failed:" + e.getMessage());
        }
        return list;
    }

    /**
     * 创建es文档数据
     *
     * @param indexRequest
     * @return
     */
    public boolean create(IndexRequest indexRequest) {
        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            int status = indexResponse.status().getStatus();
            return status == 200 || status == 201;
        } catch (Exception e) {
            log.error("create elasticsearch document failed:" + e.getMessage());
        }
        return false;
    }

    /**
     * 通过日志监控id查找报警设置
     *
     * @param
     * @return
     */
    public SearchHits searchHits(SearchRequest searchRequest) {

        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return search.getHits();
        } catch (Exception e) {
            log.error("search logAlerter error:" + e.getMessage());
        }
        return null;
    }

    /**
     * 删除索引文档
     *
     * @param deleteRequest
     */
    public void deleteDoc(DeleteRequest deleteRequest) {
        try {
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("delete document error:" + e.getMessage());
        }
    }

    /**
     * 查找文档
     *
     * @param getRequest
     * @return
     */
    public GetResponse findDocument(GetRequest getRequest) {
        try {
            return restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("find document error:" + e.getMessage());
        }
        return null;
    }

    /**
     * 更新文档
     *
     * @param updateRequest
     */
    public boolean updateDocument(UpdateRequest updateRequest) {
        try {
            UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            int status = update.status().getStatus();
            return status == 200 || status == 201;
        } catch (Exception e) {
            log.error("update document error:" + e.getMessage());
        }
        return false;
    }


    public boolean test() {
        try {
            Request request = new Request("get", "/_nodes/stats");
            Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            JsonObject jsonObject = gson.fromJson(new InputStreamReader(content), JsonObject.class);
            JsonObject nodeJson = jsonObject.getAsJsonObject("nodes");
            for (Map.Entry<String, JsonElement> entry : nodeJson.entrySet()){
                String nodeName = entry.getKey();
                JsonObject member = entry.getValue().getAsJsonObject();
                JsonPrimitive host = member.getAsJsonPrimitive("host");
                JsonObject indices = member.getAsJsonObject("indices");
                JsonObject os = member.getAsJsonObject("os");
                JsonObject process = member.getAsJsonObject("process");

            }
           return true;
        } catch (Exception e) {
            log.error("checkMappingExist failed:" + e.getMessage());
        }
        return false;
    }
}