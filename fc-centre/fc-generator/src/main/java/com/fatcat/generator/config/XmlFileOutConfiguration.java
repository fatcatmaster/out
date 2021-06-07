package com.fatcat.generator.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import static com.fatcat.generator.properties.GeneratorProperties.RESOURCE_DIR;
import static com.fatcat.generator.properties.GeneratorProperties.TEMPLATE_PATH;

/**
 * @author fatcat
 * @description xml 文件输出配置类
 * @create 2021/4/2
 **/
public class XmlFileOutConfiguration extends FileOutConfig {

    @Override
    public String outputFile(TableInfo tableInfo) {
        // 额外增加一个xml文件的输出
        return RESOURCE_DIR + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
    }

    XmlFileOutConfiguration() {
        // 设置模板文件路径
        this.setTemplatePath(TEMPLATE_PATH);
    }
}
