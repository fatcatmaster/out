package com.fatcat.elastic.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fatcat
 * @description es 的连接配置类
 * @create 2021/4/22
 **/
@SpringBootConfiguration
@EnableConfigurationProperties(ElasticValueConfiguration.class)
@Slf4j
public class ElasticSearchConfiguration {

    @Autowired
    private ElasticValueConfiguration elasticValueConfiguration;

    /**
     * 同名覆盖默认的 RestHighLevelClient
     */
    @Bean
    RestHighLevelClient restHighLevelClient() {
        log.info("elastic connection begin to set up...");
        if (!elasticValueConfiguration.isConnectEnabled()) {
            return null;
        }
        List<HttpHost> httpHosts = new ArrayList<>();
        for (String es : elasticValueConfiguration.getUris()) {
            httpHosts.add(new HttpHost(es));
        }
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // 需要用户名和密码的认证
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(elasticValueConfiguration.getName(), elasticValueConfiguration.getPassword()));
        RestClientBuilder restClientBuilder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));
        // 异步连接httpclient配置
        restClientBuilder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            httpAsyncClientBuilder.setMaxConnTotal(elasticValueConfiguration.getMaxConnect());
            httpAsyncClientBuilder.setMaxConnPerRoute(elasticValueConfiguration.getMaxConnectRoute());
            return httpAsyncClientBuilder;
        });
        // 异步连接httpclient延时配置
        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(elasticValueConfiguration.getConnectTimeout());
            requestConfigBuilder.setSocketTimeout(elasticValueConfiguration.getSocketTimeout());
            requestConfigBuilder.setConnectionRequestTimeout(elasticValueConfiguration.getRequestTimeout());
            return requestConfigBuilder;
        });
        // 设置一个监听程序，每次节点发生故障时都会收到通知，这样就可以采取相应的措施。
        restClientBuilder.setFailureListener(new RestClient.FailureListener() {
            public void onFailure(HttpHost host) {
                log.warn("elastic nodes {} error!!!", host.toHostString());
            }
        });
        log.info("elastic connection set successfully!");
        return new RestHighLevelClient(restClientBuilder);
    }
}
