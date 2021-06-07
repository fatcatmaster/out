package com.fatcat.generator.config;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.fatcat.generator.convert.MySqlConvert;

import static com.fatcat.generator.properties.CustomizeProperties.DATA_BASE_NAME;
import static com.fatcat.generator.properties.GeneratorProperties.*;

/**
 * @author fatcat
 * @description 自定义数据库配置
 * @create 2021/4/2
 **/
public class DataSourceConfiguration extends DataSourceConfig {
    public DataSourceConfiguration() {
        // 数据库的地址
        this.setUrl(String.format(DATA_URL, DATA_BASE_NAME));
        // 驱动名称
        this.setDriverName(DATA_DRIVER_NAME);
        // 数据库的用户名
        this.setUsername(DATA_USER_NAME);
        // 数据库的密码
        this.setPassword(DATA_USER_PWD);
        // 数据库字段类型转换
        this.setTypeConvert(MySqlConvert.INSTANCE);
    }
}
