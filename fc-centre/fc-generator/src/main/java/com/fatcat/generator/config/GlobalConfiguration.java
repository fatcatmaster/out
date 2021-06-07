package com.fatcat.generator.config;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;

import static com.fatcat.generator.properties.CustomizeProperties.AUTHOR;
import static com.fatcat.generator.properties.GeneratorProperties.OUT_PUT_DIR;

/**
 * @author fatcat
 * @description 自定义全局配置
 * @create 2021/4/2
 **/
public class GlobalConfiguration extends GlobalConfig {

    public GlobalConfiguration() {
        // 自动化代码的生成路径
        this.setOutputDir(OUT_PUT_DIR);
        // 代码作者签名
        this.setAuthor(AUTHOR);
        // 是否打开输出目录
        this.setOpen(Boolean.FALSE);
        // 是否覆盖已有文件
        this.setFileOverride(Boolean.TRUE);
        // 指定时间类型为 Date，默认为java8最新得LocalDateTime
        this.setDateType(DateType.ONLY_DATE);
    }

    // controller 命名方式，注意 %s 会自动填充表实体属性
    //this.setControllerName("%sController");
    // service 命名方式
    //this.setServiceName("%sService");
    // serviceImpl 命名方式
    //this.setServiceImplName("%sServiceImpl");
    // mapper 命名方式
    //this.setMapperName("%sMapper");
    // xml 命名方式
    //this.setXmlName("%sMapper");
    // 开启 swagger2 模式
    //this.setSwagger2(true);
    // 是否开启 ActiveRecord 模式
    //this.setActiveRecord(true);
    // 是否在xml中添加二级缓存配置
    //this.setEnableCache(false);
    // 是否开启 BaseResultMap
    //this.setBaseResultMap(false);
    // XML columnList
    //this.setBaseColumnList(false);
}
