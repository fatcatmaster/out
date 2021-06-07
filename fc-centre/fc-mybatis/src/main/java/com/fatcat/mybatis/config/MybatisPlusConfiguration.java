package com.fatcat.mybatis.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fatcat.mybatis.base.BaseEntity;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * @author fatcat
 * @description mybatis-plus 自定义配置
 * @create 2021/5/6
 **/
@SpringBootConfiguration
@EnableConfigurationProperties(MybatisValueConfiguration.class)
public class MybatisPlusConfiguration {

    @Autowired
    private MybatisValueConfiguration mybatisValueConfiguration;

    /**
     * 重新注入自己的属性配置，依旧采用默认的连接方式
     * 并指定优先使用自己的配置属性
     */
    @Bean
    @Primary
    public DataSourceProperties dataSourceProperties(DataSourceProperties properties) {
        properties.setUrl(mybatisValueConfiguration.getUrl());
        properties.setUsername(mybatisValueConfiguration.getName());
        properties.setPassword(mybatisValueConfiguration.getPassword());
        return properties;
    }

    /**
     * 元对象字段自动填充控制器
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            /**
             * # @TableField 注解使用 fill = INSERT_UPDATE or INSERT 会使用当前方法进行填充
             */
            @Override
            public void insertFill(MetaObject metaObject) {
                if (metaObject.hasGetter(BaseEntity.CREATE_TIME)
                        && metaObject.hasSetter(BaseEntity.CREATE_TIME)) {
                    setFieldValByName(BaseEntity.CREATE_TIME, DateUtil.now(), metaObject);
                }
                if (metaObject.hasGetter(BaseEntity.UPDATE_TIME)
                        && metaObject.hasSetter(BaseEntity.UPDATE_TIME)) {
                    setFieldValByName(BaseEntity.UPDATE_TIME, DateUtil.now(), metaObject);
                }
            }

            /**
             * # @TableField 注解使用 fill = INSERT_UPDATE or UPDATE 会使用当前方法进行填充
             * 更新时字段名称有前缀 Constants.ENTITY
             *
             * @see com.baomidou.mybatisplus.core.mapper.BaseMapper#updateById(java.lang.Object)
             */
            @Override
            public void updateFill(MetaObject metaObject) {
                if (metaObject.hasGetter(BaseEntity.UPDATE_TIME)
                        && metaObject.hasSetter(BaseEntity.UPDATE_TIME)) {
                    setFieldValByName(Constants.ENTITY + StrUtil.DOT + BaseEntity.UPDATE_TIME, DateUtil.now(), metaObject);
                }
            }
        };
    }


    /**
     * 新的分页插件,一缓和二缓遵循 mybatis 的规则
     * 需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 自定义其他配置，预留接口
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            configuration.setUseDeprecatedExecutor(Boolean.FALSE);
        };
    }
}
