package com.fatcat.core.config;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.SneakyThrows;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 针对请求数据的序列化和反序列化的增强型配置
 * 所有的 Converter 请勿简写成 lambda，否则spring会捕获不到自定义的转换器
 * 简单说明一下原因：lambda 表达式无法明确指定 Converter 的具体类型，而 spring 捕获转换器时需要 Converter 明确的类型
 * 转化器在 spring 中注入的地方如下：所以自定义转换器的实例化必须保证在装载前已经实例完成，否则无法装载
 * {@link WebMvcAutoConfiguration.EnableWebMvcConfiguration#mvcConversionService}
 * {@link WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#addFormatters}
 * {@link ApplicationConversionService#addBeans}
 *
 * @author fatcat
 * @description mvc 增强型配置
 * @create 2021/4/28
 **/
@SpringBootConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class JacksonConfiguration {

    //======================= 以下转换器在 @RequestParam 和 @PathVariable 的注解参数下生效 =============================

    /**
     * LocalDate 转换器，用于转换 RequestParam 和 PathVariable 参数
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @SneakyThrows
            @Override
            public LocalDate convert(@NonNull String source) {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
            }
        };
    }

    /**
     * LocalDateTime 转换器，用于转换 RequestParam 和 PathVariable 参数
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @SneakyThrows
            @Override
            public LocalDateTime convert(@NonNull String source) {
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
            }
        };
    }

    /**
     * LocalTime 转换器，用于转换 RequestParam 和 PathVariable 参数
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @SneakyThrows
            @Override
            public LocalTime convert(@NonNull String source) {
                return LocalTime.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN));
            }
        };
    }

    /**
     * Date转换器，用于转换 RequestParam 和 PathVariable 参数
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @SneakyThrows
            @Override
            public Date convert(@NonNull String source) {
                return DateUtil.parse(source);
            }
        };
    }


    //======================= 以下转换器针对 json 格式的请求，以及 @ResponseBody 的控制器（json 格式返回） 生效 =============================

    /**
     * 全局编码和解码设置，用于设置全局数据传输时的序列化设置
     * 目前设置仅针对日期类做了特殊处理
     * 其他类型采用 jackson 默认序列化和反序列化方式处理
     * 定义成多例，保证其他组件可以继续定制私有化序列方式
     */
    @Bean
    @Scope("prototype")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 禁用默认的时间格式序列化方式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

        // LocalDateTime 系列序列化和反序列化模块
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));

        // Date 序列化
        javaTimeModule.addSerializer(Date.class, new JsonSerializer<Date>() {
            @Override
            public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(DateUtil.format(date, DatePattern.NORM_DATETIME_FORMAT));
            }
        });
        // Date 反序列化
        javaTimeModule.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @SneakyThrows
            @Override
            public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
                return DateUtil.parse(jsonParser.getText());
            }
        });

        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}
