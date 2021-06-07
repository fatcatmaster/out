package com.fatcat.gateway.utils;

import cn.hutool.core.util.ObjectUtil;
import com.fatcat.common.constants.enums.CommonEnum;
import com.fatcat.common.constants.enums.ExceptionEnum;
import com.fatcat.common.exception.FatCatException;
import com.fatcat.gateway.context.GatewayContext;
import io.netty.buffer.ByteBufAllocator;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author fatcat
 * @description 过滤器逻辑判断工具类
 * @create 2021/4/8
 **/
public class FilterUtil {
    /**
     * 常量定义
     */
    private static final Character CHAR_AND = '&';
    private static final String STRING_AND = "&";
    private static final String STRING_EQUAL = "=";
    private static final String CHUNKED = "chunked";


    /**
     * 判断当前请求是否应该被拦截
     *
     * @return 网关上下文
     */
    public static GatewayContext getContent(ServerWebExchange exchange) {
        GatewayContext gatewayContext = exchange.getAttribute(CommonEnum.GATEWAY_CONTEXT.getKey());
        if (ObjectUtil.isNull(gatewayContext)) {
            // 不存在上下文，请求禁止
            throw new FatCatException(ExceptionEnum.REQUEST_FORBIDDEN);
        }
        return gatewayContext;
    }

    // ================================ 以下解析请求参数方法未生效，原因不明（可能 spring-cloud 版本问题） ================================

    /**
     * 读取请求体中 form-data 的数据
     *
     * @return 重新包装后的请求
     */
    public static Mono<Void> readFormData(ServerWebExchange exchange, GatewayFilterChain chain, GatewayContext gatewayContext) {
        final ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        return exchange.getFormData()
                .doOnNext(gatewayContext::setFormData)
                .then(Mono.defer(() -> {
                    MediaType contentType = headers.getContentType();
                    if (ObjectUtil.isNull(contentType)) {
                        throw new FatCatException(ExceptionEnum.REQUEST_PARSING_ERROR);
                    }
                    Charset charset = contentType.getCharset();
                    charset = ObjectUtil.defaultIfNull(charset, StandardCharsets.UTF_8);
                    String charsetName = charset.name();
                    MultiValueMap<String, String> formData = gatewayContext.getFormData();
                    if (ObjectUtil.isNull(formData) || formData.isEmpty()) {
                        // 不再解析，直接返回
                        return chain.filter(exchange);
                    }
                    StringBuilder formDataBodyBuilder = new StringBuilder();
                    try {
                        // 重新包装 form-data
                        for (Map.Entry<String, List<String>> entry : formData.entrySet()) {
                            for (String value : entry.getValue()) {
                                formDataBodyBuilder.append(entry.getKey())
                                        .append(STRING_EQUAL)
                                        .append(URLEncoder.encode(value, charsetName))
                                        .append(STRING_AND);
                            }
                        }
                    } catch (UnsupportedEncodingException e) {
                        // 忽略解码错误，只捕获不处理
                    }
                    // 删除最后多余的“&”
                    if (CHAR_AND.equals(formDataBodyBuilder.charAt(formDataBodyBuilder.length() - 1))) {
                        formDataBodyBuilder.deleteCharAt(formDataBodyBuilder.length() - 1);
                    }
                    String formDataBodyString = formDataBodyBuilder.toString();
                    // 获取数据编码（utf-8）
                    byte[] bodyBytes = formDataBodyString.getBytes(charset);
                    int contentLength = bodyBytes.length;
                    // ServerHttpRequestDecorator 用于修改请求体内容
                    ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(request) {
                        // 修改 contentLength
                        @Override
                        public HttpHeaders getHeaders() {
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.putAll(super.getHeaders());
                            if (contentLength > 0) {
                                httpHeaders.setContentLength(contentLength);
                            } else {
                                httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, CHUNKED);
                            }
                            return httpHeaders;
                        }

                        // 读取 Flux
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return DataBufferUtils.read(new ByteArrayResource(bodyBytes),
                                    new NettyDataBufferFactory(ByteBufAllocator.DEFAULT),
                                    contentLength);
                        }
                    };
                    ServerWebExchange mutateExchange = exchange.mutate().request(decorator).build();
                    return chain.filter(mutateExchange);
                }));
    }

    /**
     * 解析请求体的 body
     *
     * @return 重新封装后的请求
     */
    public static Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain, GatewayContext gatewayContext) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    // 读取 Flux，并释放缓冲区
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                        DataBufferUtils.retain(buffer);
                        return Mono.just(buffer);
                    });
                    // 重新封装 ServerHttpRequest
                    ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        @SuppressWarnings("NullableProblems")
                        public Flux<DataBuffer> getBody() {
                            return cachedFlux;
                        }
                    };
                    // 将请求体重新塞进 exchange 中
                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                    // 读取 body 数据
                    return ServerRequest
                            .create(mutatedExchange, HandlerStrategies.withDefaults().messageReaders())
                            .bodyToMono(String.class)
                            // 将 body 保存在上下文中
                            .doOnNext(gatewayContext::setJsonBody)
                            .then(chain.filter(mutatedExchange));
                });
    }

}
