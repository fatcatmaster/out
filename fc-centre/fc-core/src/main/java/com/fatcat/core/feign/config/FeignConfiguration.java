package com.fatcat.core.feign.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.stream.Collectors;

/**
 * feign 的相关配置
 *
 * @author fatcat
 * @description feign 的相关配置
 * @create 2021/4/9
 **/
@SpringBootConfiguration
public class FeignConfiguration {

    /**
     * feign 接口数据传输转换器，方法名即bean-id
     * ConditionalOnMissingBean 是通过bean-id判断
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        // 使用默认的数据传输转换器即可
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

}
