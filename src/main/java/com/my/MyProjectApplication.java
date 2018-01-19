package com.my;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 类注释
 *
 * @version 1.0.0
 * @author: wuyx
 * @date: 2017/6/14
 * @time: 9:29
 * @see: 链接到其他资源
 * @since: 1.0
 */
@Configuration
@ComponentScan("com.my")
/**
 * 使用springboot扫描的两种注解配置方式：
 1、@Controller
 　 @EnableAutoConfiguration
　　@ComponentScan
 2、@SpringBootApplication
    @SpringBootApplication注解等价于以默认属性使用@Configuration，@EnableAutoConfiguration和@ComponentScan
 */

/**
 * @EnableAutoConfiguration作用：Spring Boot会自动根据你jar包的依赖来自动配置项目。
 * 例如当你项目下面有HSQLDB的依赖时，Spring Boot会创建默认的内存数据库的数据源DataSource，
 * 如果你自己创建了DataSource，Spring Boot就不会创建默认的DataSource。
   如果你不想让Spring Boot自动创建，你可以配置注解的exclude属性:
   @EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
 */
@EnableAutoConfiguration

@SpringBootApplication
@MapperScan(basePackages = "com.my.mapper")
/**
 * 对于需要部署到传统servlet容器之中的应用，Boot提供了一种方式以编码的方式初始化Web配置。
 * 为了使用这一点，Boot提供了可选的WebApplicationInitializer，它会使用servlet容器来注册应用，
 * 这会通过Servlet 3.0 API以编码的方式注册servlet并且会用到ServletContext。
 * 通过提供SpringBootServletInitializer的子类，Boot应用能够使用嵌入的Spring上下文来注册配置，
 * 这个Spring上下文是在容器初始化的时候创建的
 */

/** 注册服务-服务端(eureka-service)*/
//@EnableEurekaServer
/** 服务提供者-客户端(eureka-client)*/
@EnableEurekaClient
public class MyProjectApplication extends SpringBootServletInitializer {
    @Bean //定义REST客户端，RestTemplate实例
    @LoadBalanced //开启负债均衡的能力
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     *   SpringApplication包含了一些其他可以配置的方法，如果你想做一些配置，可以用这种方式。
         除了上面这种直接的方法外，还可以使用SpringApplicationBuilder：
         new SpringApplicationBuilder()
         .showBanner(false)
         .sources(Application.class)
         .run(args);
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(MyProjectApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MyProjectApplication.class);
    }
}
