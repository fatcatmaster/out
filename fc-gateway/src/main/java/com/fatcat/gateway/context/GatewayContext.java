package com.fatcat.gateway.context;

import cn.hutool.core.util.IdUtil;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;

/**
 * @author fatcat
 * @description 网关上下文
 * @create 2021/4/14
 **/
@Data
public class GatewayContext implements Serializable {
    private static final long serialVersionUID = -2515209630547273933L;
    /**
     * 自定义请求id
     */
    private String requestId;
    /**
     * 自定义请求通行证
     * true: 不校验直接放行
     * false: 继续下一个过滤器的校验(缺省值)
     */
    private Boolean requestPass;
    /**
     * 缓存完整的请求路径（带服务器地址）
     */
    private String requestUri;
    /**
     * 缓存请求路径（不带服务器地址）
     */
    private String requestPath;
    /**
     * 请求类型，即 header 里面的 Content-Type
     */
    private MediaType contentType;
    /**
     * 缓存请求方式
     */
    private HttpMethod requestMethod;
    /**
     * 缓存头部数据
     */
    private HttpHeaders requestHeaders;
    /**
     * 请求参数（GET、DELETE)
     */
    private String params;
    /**
     * 缓存 json 请求体（POST、PUT）
     */
    private String jsonBody;
    /**
     * 缓存 form-data 请求体（POST、PUT）
     */
    private MultiValueMap<String, String> formData;

    public GatewayContext() {
        this.requestId = IdUtil.simpleUUID();
        this.requestPass = Boolean.FALSE;
    }
}
