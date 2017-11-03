package com.my.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ConfigurationProperties 声明完成之后，Boot将会识别出它是一个配置对象，
 * 并且会按照运行时classpath之中application.properties或application.yml文件中的配置指令填充它的属性
 *
 * @version 1.0.0
 * @author: wuyx
 * @date: 2017/10/31
 * @time: 14:56
 * @see: 链接到其他资源
 * @since: 1.0
 */
@Component
@ConfigurationProperties(value = "server")
public class ConfigProperties {
    private String port;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
