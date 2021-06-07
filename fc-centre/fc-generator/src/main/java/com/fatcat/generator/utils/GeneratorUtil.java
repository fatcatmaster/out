package com.fatcat.generator.utils;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.fatcat.generator.config.*;

/**
 * @author fatcat
 * @description 代码生成器
 * @create 2021/4/2
 **/
public class GeneratorUtil {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        mpg.setGlobalConfig(new GlobalConfiguration());
        // 数据源配置
        mpg.setDataSource(new DataSourceConfiguration());
        // 包配置
        mpg.setPackageInfo(new PackageConfiguration());
        // 自定义输出配置，自定义配置会被优先输出
        mpg.setCfg(new InjectionConfiguration());
        // 配置模板
        mpg.setTemplate(new TemplateConfiguration());
        // 策略配置
        mpg.setStrategy(new StrategyConfiguration());
        // 设置引擎模板
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
