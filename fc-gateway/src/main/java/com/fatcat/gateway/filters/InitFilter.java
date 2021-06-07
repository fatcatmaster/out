package com.fatcat.gateway.filters;

import cn.hutool.json.JSONUtil;
import com.fatcat.common.constants.enums.CommonEnum;
import com.fatcat.common.constants.enums.ExceptionEnum;
import com.fatcat.common.constants.interfaces.OrderConstants;
import com.fatcat.common.exception.FatCatException;
import com.fatcat.gateway.context.GatewayContext;
import com.fatcat.gateway.utils.FilterUtil;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author fatcat
 * @description 初始化过滤器相关参数
 * @create 2021/4/8
 **/
@SpringBootConfiguration
public class InitFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 缓存请求路径
        String path = request.getPath().pathWithinApplication().value();
        HttpMethod requestMethod = request.getMethod();
        HttpHeaders requestHeaders = request.getHeaders();
        MediaType mediaType = requestHeaders.getContentType();
        // 创建一个自定义网管上下文，用于保存自定义信息
        GatewayContext gatewayContext = new GatewayContext();
        // 完整uri
        gatewayContext.setRequestUri(request.getURI().toString());
        // 保存路径
        gatewayContext.setRequestPath(path);
        // 当前请求方式
        gatewayContext.setRequestMethod(requestMethod);
        // 当前媒体类型，即数据类型（json or form-data more）
        gatewayContext.setContentType(mediaType);
        // 保存头部信息
        gatewayContext.setRequestHeaders(requestHeaders);
        // 默认将当前请求传递给下个过滤器
        Mono<Void> mono = chain.filter(exchange);
        if (HttpMethod.POST.equals(requestMethod) || HttpMethod.PUT.equals(requestMethod)) {
            // 对解析过后的 request 需要重新分装，然后传给下个过滤器，否则下个过滤器无法获得到正确的 request
            if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                // 获取 body 数据，需要解析request，解析后重新封装 TODO 方法未生效
                mono = FilterUtil.readBody(exchange, chain, gatewayContext);
            } else if (MediaType.APPLICATION_FORM_URLENCODED.equals(mediaType)) {
                // 获取 form-data 数据，需要解析request，解析后重新封装 TODO 方法未生效
                mono = FilterUtil.readFormData(exchange, chain, gatewayContext);
            }
        } else if (HttpMethod.GET.equals(requestMethod) || HttpMethod.DELETE.equals(requestMethod)) {
            gatewayContext.setParams(JSONUtil.toJsonStr(request.getQueryParams().toSingleValueMap()));
        } else {
            // 非指定请求类型不允许通行
            throw new FatCatException(ExceptionEnum.REQUEST_FORBIDDEN);
        }
        // 保存网关上下文到 exchange 中
        exchange.getAttributes().put(CommonEnum.GATEWAY_CONTEXT.getKey(), gatewayContext);
        // 交付给下一个过滤器
        return mono;
    }

    @Override
    public int getOrder() {
        return OrderConstants.INIT_FILTER_ORDER;
    }
}
