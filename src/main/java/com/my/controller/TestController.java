package com.my.controller;

import com.my.config.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类注释
 *
 * @version 1.0.0
 * @author: wuyx
 * @date: 2017/10/31
 * @time: 15:17
 * @see: 链接到其他资源
 * @since: 1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ConfigProperties configProperties;

    @GetMapping("/config")
    public String configValue() {
        System.out.println("==================="+configProperties.getPort()+"===================");
        return configProperties.getPort();
    }
}
