# myTestProject -->springbootWeb

# 项目开发规范
====

## 项目开发中涉及到相关主要的技术框架
项目管理:gradle
版本管理:Git
框架:SpringBoot,Mybatis


## 项目介绍
    * springBoot，gradle，mybatis 搭建的web应用demo

## 模块介绍
    ##  conf 公共辅助
        *  及所谓的工具模块,提供公用辅助服务
     
    ##  WebApplication 项目入口
        *  启动项目
    ##  controller 控制层 不做业务处理
            *  
    ##  mapper 定义mapper接口
            *  
    ##  model 实体类
            *  
    ##  mapping 自动生成的xml 在resource/mapping里。
                *    
    ##  service 业务实现
               * 
## 自动生成步骤
    ##  generatorConfig.xml中 修改tableName ／domainObjectName 
    ##  执行mybatisGenerate task
## 编码及开发中注意事项
    * 所有你编写过的任何代码一定要有注释,注释要清晰明了,避免后期维护出问题,且无用代码及时清除
    * 代码过程中拒绝硬编码,通过常量或枚举来处理,每个模块的返回信息用自己模块的枚举类
    * 方法体尽量保持简洁,类名方法及变量取名尽量做到见名知意
    * 所有异常捕获均由最外层进行抓取,内部逻辑判断以异常形式进行抛出并设定错误代码
    * 在需要的地方使用new构造对象,非必要时不要提前构造对象
    * 防御式编程思想,尽早发现问题并返回错误编码
    * 日志打印,保证入参出参具有日志输出,日志要有关键字,方便后续问题协查及定位,敏感信息脱敏打印
    * 代码中和环境无关的相关配置尽量避免放到配置文件中
    * 接口调用时必须明确各个接口响应的状态(区分好状态[通信状态和最终业务实际状态],并做正确的业务逻辑处理)
    * 接口变动及升级请做好对应的文档变更并及时同步至Git上,保证对应接口和文档中的一致性

### 消除IDE的警告
    * 没有用到的变量、方法去掉
    * 没有用到注释请直接去掉
    * List,Map 请指定具体的类型,如List<String>,Map<String,Integer>
    * 序列化要求:通过接口传递的对象必须序列化,且务必加上serialVersionUID:
        （1）setting->Inspections->Serialization issues，将serialzable class without "serialVersionUID"打上勾
        （2）将光标放到类名上，按atl＋enter键，就会提示生成serialVersionUID了。

## 类注释模板
    * 类注释IDEA开发工具添加方式为：步骤：settings-->file and code Templates-->选择incudes 进行配置

```
/**
 * 类注释
 * @author ${USER}
 * @version 1.0.0 createTime: ${DATE} ${TIME}
 * @see 链接到其他资源
 * @since 1.0
 */
 ```

