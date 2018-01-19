package com.my.controller;

import com.my.config.ConfigProperties;
import com.my.domain.WebDto;
import com.my.mapper.WebDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

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
    @Autowired
    WebDtoMapper webDtoMapper;
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/config")
    public String configValue() {
        System.out.println("==================="+configProperties.getPort()+"===================");
        System.out.println("total:" + webDtoMapper.selectTotal());
        return configProperties.getPort();
    }

    /**
     * 呗SERVICE.CLIENT服务调用时会由ribbon轮询调用(默认)，端口号port也会互换（服务中有多少应用就有多少个端口）
     * @param id
     * @param req
     * @return
     */
    @GetMapping("selectWebDtoById")
    @ResponseBody
    public WebDto selectWebDtoById(Long id, HttpServletRequest req) {
        System.out.println("==========id:" + id + "==========port:" + req.getLocalPort());
        return webDtoMapper.selectByPrimaryKey(id);
    }

    @GetMapping("findWDById")
    @ResponseBody
    public WebDto findWDById(Long id, HttpServletRequest req) {
        // 调用服务SERVICE.CLIENT接口:selectWebDtoById
        WebDto s = restTemplate.getForObject("http://SERVICE.CLIENT/test/selectWebDtoById?id="+id, WebDto.class);
        System.out.println("==========res:"+ s + "==========" + "port:" + req.getLocalPort());
        return s;
    }

    /**
     * 测试下外网，也就是如果域名是外网的，不在eureka注册服务中的，会怎样
     * @return
     */
    @GetMapping(value = "/baidu")
    @ResponseBody
    public String baidu() {
        //url中对应api提供者的名称，全大写
        return restTemplate.getForEntity("http://www.baidu.com/", String.class).getBody();
    }
}
