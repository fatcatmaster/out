package com.fatcat.core.feign.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatcat.core.config.JacksonConfiguration;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.Resource;

/**
 * 注意事项：此编码器和解码器基于 json 格式的处理
 * 对于路径参数不起作用，但是路径参数 feign 会使用默认的方式封装在 map 中后处理
 * 所以路径参数中对于 jackson 默认序列化方式不支持的参数会报错，提示转换错误
 * 目前发现不支持的参数对象有 LocalDateTime、LocalDate、LocalTime（使用字符串时间格式传参时不支持）
 * 所以建议 feign 接口不要使用非 POST、GET 的请求方式，GET 尽量传递实体对象
 * 当然，如果参数中不包含（LocalDateTime、LocalDate、LocalTime）等不支持的对象类型，可以随意传参；
 *
 * @author fatcat
 * @description feign 接口相关的编码器和解码器
 * @create 2021/4/28
 **/
@SpringBootConfiguration
@ConditionalOnBean(ObjectMapper.class)
@AutoConfigureAfter(JacksonConfiguration.class)
public class FeignCoder {

    @Resource(name = "objectMapper")
    private ObjectMapper objectMapper;

    /**
     * feign 接口的编码器，用于处理请求参数，仅针对json数据
     */
    @Bean
    public Encoder encoder() {
        HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        ObjectFactory<HttpMessageConverters> messageConverters = () -> new HttpMessageConverters(converter);
        return new SpringEncoder(messageConverters);
    }

    /**
     * feign 接口的解码器，用于处理接口响应结果，json结果序列化
     */
    @Bean
    public Decoder decoder() {
        HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        ObjectFactory<HttpMessageConverters> messageConverters = () -> new HttpMessageConverters(converter);
        return new SpringDecoder(messageConverters);
    }

    /**
     * feign 接口针对响应状态码在[200, 300)之间的请求，会调用此错误解码器进行处理
     * 由于 feign 对于请求非200的响应请求会抛出异常，异常会被全局异常中心捕获后处理
     * 所以当前错误解码器获取到的数据已经是全局异常中心处理过的数据了
     * <p>
     * # @see feign.SynchronousMethodHandler#executeAndDecode
     */
//    @Bean
    public ErrorDecoder errorDecoder() {
        // 使用默认的错误解码器，如果有需要可以自定义
        return new ErrorDecoder.Default();
        // 记录获取 response 中的数据方法： String json = IoUtil.read(response.body().asInputStream(), StandardCharsets.UTF_8);
    }
}
