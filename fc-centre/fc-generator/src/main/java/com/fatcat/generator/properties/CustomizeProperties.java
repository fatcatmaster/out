package com.fatcat.generator.properties;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;

/**
 * @author fatcat
 * @description 定制常量
 * @create 2021/4/2
 **/
public class CustomizeProperties {
    /**
     * 作者签名
     */
    public static final String AUTHOR = "fatcat";

    /**
     * 数据库名称
     */
    public static final String DATA_BASE_NAME = "fc_user";

    /**
     * 数据表名称，多个使用 “,” 分割
     */
    public static final String DATA_TABLE_NAME = "fc_user";

    /**
     * 数据表的前缀
     */
    public static final String PREFIX = "fc_";

    /**
     * 多个用逗号隔开，按照顺序写
     */
    public static final List<String> EX_MODULE_SUFFIX = CollUtil.newArrayList("app");
}
