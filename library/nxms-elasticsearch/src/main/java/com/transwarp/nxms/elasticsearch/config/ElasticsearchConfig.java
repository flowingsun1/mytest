package com.transwarp.nxms.elasticsearch.config;

import com.google.gson.Gson;
import com.transwarp.nxms.elasticsearch.service.NxmsElasticsearchClient;
import com.transwarp.nxms.elasticsearch.service.NxmsElasticsearchService;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ZGW
 * @Date 2019/03/13
 */
@Configuration
@EnableConfigurationProperties({TranswarpElasticsearchProperties.class})
public class ElasticsearchConfig {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(RestHighLevelClient.class)
    public RestHighLevelClient restHighLevelClient(TranswarpElasticsearchProperties elasticsearchProperties) {
        HttpHost[] httpHosts = new HttpHost[elasticsearchProperties.getHost().length];
        for (int i = 0; i < httpHosts.length; i++) {
            httpHosts[i] = new HttpHost(elasticsearchProperties.getHost()[i], elasticsearchProperties.getPort(), null);
        }
        return new RestHighLevelClient(RestClient.builder(httpHosts));
    }

    @Bean
    public NxmsElasticsearchClient nxmsElasticsearchClient(RestHighLevelClient restHighLevelClient) {
        return new NxmsElasticsearchClient(restHighLevelClient, gson());
    }

    @Bean
    public NxmsElasticsearchService nxmsElasticsearchService(NxmsElasticsearchClient nxmsElasticsearchClient) {
        return new NxmsElasticsearchService(nxmsElasticsearchClient);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(Gson.class)
    public Gson gson() {
        return new Gson();
    }

}
