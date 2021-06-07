package com.fatcat.mybatis.env;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;


/**
 * spring boot 环境变量配置类
 * 以下为作用说明：
 * 在一个没有启动器的依赖中，如何让引用了当前依赖的模块加载到依赖中的自定义配置参数
 *
 * @author fatcat
 * @description spring boot 环境变量配置类
 * @create 2021/4/16
 **/
@SpringBootConfiguration
public class EnvProcessor implements EnvironmentPostProcessor {

    /**
     * 程序初始化会自动装配
     */
    private final Properties properties = new Properties();

    /**
     * 配置文件名称列表
     */
    private String[] profiles = {
    };

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        for (String profile : profiles) {
            Resource resource = new ClassPathResource(profile);
            environment.getPropertySources().addLast(loadProfiles(resource));
        }
    }

    private PropertySource<?> loadProfiles(Resource resource) {
        if (!resource.exists()) {
            throw new IllegalArgumentException("file " + resource + " not exist");
        }
        try {
            properties.load(resource.getInputStream());
            return new PropertiesPropertySource(Objects.requireNonNull(resource.getFilename()), properties);
        } catch (NullPointerException nu) {
            throw new IllegalStateException("resource " + resource + "'s name is null", nu);
        } catch (IOException ex) {
            throw new IllegalStateException("load resource exception " + resource, ex);
        }
    }
}
