package com.fatcat.generator.config;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;

/**
 * @author fatcat
 * @description 配置默认模板
 * @create 2021/4/2
 **/
public class TemplateConfiguration extends TemplateConfig {
    public TemplateConfiguration() {
        // 根据 baomidou 提供的默认模板文件进行配置，null表示取消默认使用自定义配置
        // 默认配置的路径 com.baomidou.mybatisplus-plus-generator 下的 templates
        this.setXml(null);
    }
}
