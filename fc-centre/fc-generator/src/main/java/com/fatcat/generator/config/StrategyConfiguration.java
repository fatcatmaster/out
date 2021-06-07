package com.fatcat.generator.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.fatcat.mybatis.base.BaseEntity;

import static com.fatcat.generator.properties.CustomizeProperties.DATA_TABLE_NAME;
import static com.fatcat.generator.properties.CustomizeProperties.PREFIX;

/**
 * @author fatcat
 * @description 自定义策略配置
 * @create 2021/4/2
 **/
public class StrategyConfiguration extends StrategyConfig {

    public StrategyConfiguration() {
        // 数据表名下划线转驼峰
        this.setNaming(NamingStrategy.underline_to_camel);
        // 表字段名下划线转驼峰
        this.setColumnNaming(NamingStrategy.underline_to_camel);
        // 是否加上 lombok 注解
        this.setEntityLombokModel(Boolean.TRUE);
        // Restful 风格控制器
        this.setRestControllerStyle(Boolean.TRUE);
        // 是否生成序列化id
        this.setEntitySerialVersionUID(Boolean.FALSE);
        // 是否给实体类字段加上注解
        this.setEntityTableFieldAnnotationEnable(Boolean.TRUE);
        // 需要生成的表
        this.setInclude(DATA_TABLE_NAME.split(StrUtil.COMMA));
        // 自动删除表的预设前缀
        this.setTablePrefix(PREFIX.split(StrUtil.COMMA));
        // 父类的名称(路径)
        this.setSuperEntityClass(BaseEntity.class);
        // 写于父类中的公共字段
        this.setSuperEntityColumns(BaseEntity.CREATE_USER, BaseEntity.CREATE_TIME,
                BaseEntity.UPDATE_USER, BaseEntity.UPDATE_TIME, BaseEntity.IS_DELETED);
    }

    // 公共父类                         this.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
    // 映射地址驼峰转连字符(‘-’连接)      this.setControllerMappingHyphenStyle(Boolean.TRUE);

}
