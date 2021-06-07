package com.fatcat.web.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Converter 注册bean请勿简写成 lambda 格式
 *
 * @author fatcat
 * @description 接口请求配置类
 * @create 2021/4/13
 **/
@SpringBootConfiguration
public class WebMvcConfiguration {

    /**
     * 当禁用掉 spring 的默认静态资源处理器时，注入此 bean，用于将自定义的静态资源添加至资源处理中
     * 简单说明：禁用默认静态资源处理器会默认屏蔽掉 resources 下的所有静态资源，包括自定义资源，需要手动再添加至处理器中
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.resources", name = "add-mappings", havingValue = "false")
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                /*
                 * 举例说明下面配置用途：
                 * 将 resourceLocations 下面的资源全部映射至 resourceHandler 中
                 * resourceHandler 即请求路径，可以使用匹配路径，如： /my-static/**
                 * 配置完成后通过指定路径访问资源就可以访问到了，不会因为 spring.resources.add-mappings = false 而请求不到资源
                 * 参数 resourceChain 在生产环境配置为 true，开发环境配置为 false（默认值），resourceChain 即对资源的一个缓存设置

                 registry.addResourceHandler("/swagger-ui.html")
                 .addResourceLocations("classpath:/META-INF/resources/", "/static", "/public")
                 .resourceChain(Boolean.FALSE);
                 */
            }
        };
    }
}
