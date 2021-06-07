package com.fatcat.core.feign.interceptor;

import cn.hutool.core.util.ObjectUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign 全局请求拦截器，主要目的是保留 feign 接口原有请求的头部信息
 * 注：头部信息传递仅在 信号量 的模式下生效，线程池模式会新开线程丢失原有的请求头
 * {@link com.fatcat.core.feign.config.HystrixValueConfiguration strategy}
 *
 * @author zhouhc
 * @description feign 全局请求拦截器
 * @create 2021/5/28
 **/
@SpringBootConfiguration
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 获取请求体
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNull(requestAttributes)) {
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        if (ObjectUtil.isNull(request)) {
            return;
        }
        Enumeration<String> headerNames = request.getHeaderNames();
        while (ObjectUtil.isNotNull(headerNames) && headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            requestTemplate.header(headerName, request.getHeader(headerName));
        }
    }
}
