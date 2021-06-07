package com.fatcat.mybatis.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fatcat
 * @description mybatis 数据库连接配置
 * @create 2021/5/6
 **/
@ConfigurationProperties(prefix = "spring.mybatis", ignoreInvalidFields = true)
@Data
public class MybatisValueConfiguration {
    /**
     * 需要使用的数据库名称
     */
    private String database;
    /**
     * 数据库服务器地址
     */
    private String url;
    /**
     * 数据库服务器用户名
     */
    private String name;
    /**
     * 数据库服务器密码
     */
    private String password;
    /**
     * 使用自定义编码为 utf-8;默认gbk
     */
    private boolean useUnicode = true;
    /**
     * 配置上个属性使用，指定编码集
     */
    private String characterEncoding = "utf8";
    /**
     * 指定时区，也可配置成"GMT+8"，但特殊字符'+'需要转换成十六进制'%2B',即="GMT%2B8"
     */
    private String serverTimezone = "Asia/Shanghai";
    /**
     * 高版本mysql需要指定是否使用ssl连接
     */
    private boolean ssl = false;
    /**
     * 断开自动连接
     */
    private boolean autoReconnect = true;

    /**
     * 自行拼接数据库的地址
     */
    public String getUrl() {
        StringBuilder address = new StringBuilder();
        address.append(this.url).append(StrUtil.SLASH).append(this.database).append("?")
                .append(useUnicode ? "useUnicode=true&characterEncoding=" + this.characterEncoding + "&" : "")
                .append("serverTimezone=").append(this.serverTimezone).append("&")
                .append("useSSL=").append(this.ssl).append("&")
                .append("autoReconnect=").append(this.autoReconnect);
        return address.toString();
    }

}
