package com.fatcat.generator.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;

import java.util.List;

/**
 * @author fatcat
 * @description 自定义输出配置
 * @create 2021/4/2
 **/
public class InjectionConfiguration extends InjectionConfig {

    public InjectionConfiguration() {
        // xml 文件自定义
        FileOutConfig xmlConfig = new XmlFileOutConfiguration();
        // mapStruct 文件自定义
        FileOutConfig mapConfig = new MapFileOutConfiguration();
        // controller 文件自定义
        // service 文件自定义
        // serviceImpl 文件自定义
        // mapper 文件自定义
        List<FileOutConfig> configList = CollUtil.newArrayList(xmlConfig, mapConfig);
        this.setFileOutConfigList(configList);
    }

    @Override
    public void initMap() {

    }
}
