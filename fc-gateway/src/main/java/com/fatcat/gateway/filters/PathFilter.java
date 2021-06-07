package com.fatcat.gateway.filters;

import cn.hutool.core.util.ReUtil;
import com.fatcat.common.constants.interfaces.OrderConstants;
import com.fatcat.gateway.context.GatewayContext;
import com.fatcat.gateway.utils.FilterUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author fatcat
 * @description 路径过滤器
 * @create 2021/4/8
 **/
@SpringBootConfiguration
public class PathFilter implements GlobalFilter, Ordered {
    /**
     * 配置中心的不过滤路径
     */
    @Value("${fatcat.gateway.ignore-paths}")
    List<String> ignorePaths;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取上下文
        GatewayContext gatewayContext = FilterUtil.getContent(exchange);
        // 是否通行
        if (gatewayContext.getRequestPass()) {
            return chain.filter(exchange);
        }
        // 请求路径
        String requestPath = gatewayContext.getRequestPath();
        // 配置中心的路径，直接放行
        for (String ignorePath : ignorePaths) {
            if (ReUtil.isMatch(ignorePath, requestPath)) {
                // 不再过滤，所有自定义过滤器全部放行
                gatewayContext.setRequestPass(Boolean.TRUE);
                return chain.filter(exchange);
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return OrderConstants.PATH_FILTER_ORDER;
    }
}
