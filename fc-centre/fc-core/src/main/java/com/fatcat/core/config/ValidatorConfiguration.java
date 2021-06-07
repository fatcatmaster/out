package com.fatcat.core.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;

/**
 * @author fatcat
 * @description 参数校验的配置类
 * @create 2021/4/28
 **/
@SpringBootConfiguration
public class ValidatorConfiguration {
    /**
     * 当且仅当配置 customize.validator.fail-fast = true 才会生效，其他值或者缺省以下bean失效
     * 开启时请同步开启bean的覆盖，即spring.main.allow-bean-definition-overriding=true
     */
    @Bean
    @ConditionalOnProperty(name = "fatcat.validator.fail-fast")
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(
                Validation.byProvider(HibernateValidator.class)
                        .configure()
                        // failFast的意思只要出现校验失败的情况，就立即结束校验，不再进行后续的校验。
                        .failFast(Boolean.TRUE)
                        .buildValidatorFactory()
                        .getValidator()
        );
        return methodValidationPostProcessor;
    }
}
