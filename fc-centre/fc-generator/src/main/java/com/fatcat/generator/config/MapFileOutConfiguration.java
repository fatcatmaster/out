package com.fatcat.generator.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import static com.fatcat.generator.properties.GeneratorProperties.*;

/**
 * @author fatcat
 * @description mapstruct 生成模板
 * @create 2021/5/18
 **/
public class MapFileOutConfiguration extends FileOutConfig {
    @Override
    public String outputFile(TableInfo tableInfo) {
        return OUT_PUT_DIR + COMPLETE_URL + "/converter/"
                + tableInfo.getEntityName() + "Converter" + StringPool.DOT_JAVA;
    }

    MapFileOutConfiguration() {
        // 设置模板文件路径
        this.setTemplatePath(MAP_STRUCT_PATH);
    }
}
