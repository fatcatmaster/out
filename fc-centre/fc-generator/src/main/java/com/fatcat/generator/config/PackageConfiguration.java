package com.fatcat.generator.config;

import com.baomidou.mybatisplus.generator.config.PackageConfig;

import static com.fatcat.generator.properties.GeneratorProperties.MODULE_NAME;
import static com.fatcat.generator.properties.GeneratorProperties.PARENT_NAME;

/**
 * @author fatcat
 * @description 自定义包配置
 * @create 2021/4/2
 **/
public class PackageConfiguration extends PackageConfig {
    public PackageConfiguration() {
        // 包模型名称
        this.setModuleName(MODULE_NAME);
        // 父类包路径
        this.setParent(PARENT_NAME);
    }
}
