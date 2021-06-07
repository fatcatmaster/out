package com.fatcat.generator.properties;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;

import static com.fatcat.generator.properties.CustomizeProperties.*;

/**
 * @author fatcat
 * @description 生成器参数配置
 * @create 2021/4/2
 **/
public class GeneratorProperties {

    /**
     * 文件输出根目录
     */
    public static final String SYSTEM_DIR = System.getProperty("user.dir");

    /**
     * 代码生成的工程目录名称
     */
    public static final String GENERATOR_CONTEXT = "/code";

    /**
     * 自定义java文件输出目录
     */
    public static final String OUT_PUT_DIR = SYSTEM_DIR + GENERATOR_CONTEXT + "/java/";

    /**
     * 自定义资源文件的输出目录
     */
    public static final String RESOURCE_DIR = SYSTEM_DIR + GENERATOR_CONTEXT + "/resources/mapper/";

    /**
     * 数据库地址
     */
    public static final String DATA_URL = "jdbc:mysql://mysql.fatcat.fan/%s?useUnicode=true&useSSL=false&characterEncoding=utf8";

    /**
     * JDBC驱动
     */
    public static final String DATA_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库用户名
     */
    public static final String DATA_USER_NAME = "root";

    /**
     * 数据库密码
     */
    public static final String DATA_USER_PWD = "zhcan1314!@#";

    /**
     * 父级包名
     */
    public static final String PARENT_NAME = "com.fatcat";

    /**
     * 模板文件的路径，默认在 resources 下面，后缀名 ftl 对应模板引擎
     * 优先本地文件，否则在路径 com.baomidou.mybatis-plus-generator.templates 下寻找
     */
    public static final String TEMPLATE_PATH = "/templates/mapper.xml.ftl";
    public static final String MAP_STRUCT_PATH = "/templates/converter.java.ftl";

    /**
     * 模型名称，请按照自己的规则修改
     * 本人规则如下：
     * 服务名 = 数据库名，均使用相同前缀
     * 数据库中表明同样使用相同前缀
     * 模型名 = 服务名 - 前缀
     * 故此时模型名 = 数据库名 - 前缀
     * 包含自定义后缀
     */
    public static String MODULE_NAME = DATA_TABLE_NAME.replaceFirst(PREFIX, "");

    /**
     * 完整包路径，分隔符是 .
     */
    public static String COMPLETE_PACKAGE = "";

    /**
     * 完整文件路径，分隔符是 /
     */
    public static String COMPLETE_URL = "";

    static {
        // 重构包路径
        StringBuilder builder = new StringBuilder();
        builder.append(MODULE_NAME);
        for (String ex : EX_MODULE_SUFFIX) {
            builder.append(StrUtil.DOT).append(ex);
        }
        MODULE_NAME = builder.toString();
        COMPLETE_PACKAGE = PARENT_NAME + StrUtil.DOT + MODULE_NAME;
        // 重构文件路径
        COMPLETE_URL = COMPLETE_PACKAGE.replace(StrUtil.DOT, StringPool.SLASH);
    }

}
