package com.fatcat.gateway.exception;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * 当前仅当容器中存在 ServerProperties 和 ResourceProperties 时执行如下配置
 * EnableConfigurationProperties 作用保证参数中的自动配置类生效
 *
 * @author fatcat
 * @description 网关异常配置
 * @create 2021/4/14
 **/
@SpringBootConfiguration
@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class})
public class ExceptionProperties {

    private final ServerProperties serverProperties;

    private final ResourceProperties resourceProperties;

    private final ApplicationContext applicationContext;

    private final List<ViewResolver> viewResolvers;

    private final ServerCodecConfigurer serverCodecConfigurer;

    /**
     * 配置类的构造，会在程序初始化由系统调用，参数对象直接使用容器中的bean，找不到bean直接初始化失败报错
     *
     * @param serverProperties      IOC 容器中的 id = serverProperties 的 bean
     * @param resourceProperties    IOC 容器中的 id = resourceProperties 的 bean
     * @param viewResolversProvider IOC 容器中的 id = viewResolversProvider 的 bean
     * @param serverCodecConfigurer IOC 容器中的 id = serverCodecConfigurer 的 bean
     * @param applicationContext    IOC 容器中的 id = applicationContext 的 bean
     */
    public ExceptionProperties(ServerProperties serverProperties,
                               ResourceProperties resourceProperties,
                               ObjectProvider<List<ViewResolver>> viewResolversProvider,
                               ServerCodecConfigurer serverCodecConfigurer,
                               ApplicationContext applicationContext) {
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
        this.resourceProperties = resourceProperties;
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes) {
        ExceptionHandler exceptionHandler = new ExceptionHandler(
                errorAttributes,
                this.resourceProperties,
                this.serverProperties.getError(),
                this.applicationContext);
        exceptionHandler.setViewResolvers(this.viewResolvers);
        exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }

}
