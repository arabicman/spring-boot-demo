# SpringBoot练习笔记

## 01.项目搭建

(1)创建一个maven空项目命名为spring-boot-demo，作为parent。

(2)新建一个module，并选择spring-boot-demo为parent.

(3)使用module中的pom.xml文件用作模板，配置后续使用Spring Initializr新建module中的pom.xml

	*  复制<parent></parent>标签中的内容
	*  删除<groupId></groupId>标签
	*  <verision></version>可以删除或与parent保持一致
	*  在parent的pom.xml中<modules></modules>中添加module

(4)之后就可以使用Spring Initializr创建module了。



## 02. Hello World
(1) Spring Initializr: 选Spring Web。

(2) 按照01.(3)步骤修改pom.xml 并导入依赖。

(3) 修改application.properties(或者application.yml)。

(4) 写一个简单的HelloController在com.demo.demo02.controller包下。

(5) 运行SpringBootApplication并启用SpringBootRunDashboard（IntelliJ IDE）。



## 03. 从Properties中读取数据

(1)两种注入property方式:

```java
//方式一： Values注解（用于variable）
@Values("${application.name}")
private String name;
//方式二: ConfigurationProperties注解（用于class）
@Data
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperty{
  private String name;
  private String version;
}

```

(2)使用application.yml读取application-prod.yml中的数据

```yml
spring: #yaml配置
  profiles:
    active: prod
```

``` properties
# 如果是application.properties(无分号)
spring.profiles.active=dev

```

另外，也可以写Configuration类并使用@Profile("dev")注解。



## 04. Spring Boot Actuator 使用

Initializr: **Spring Web** + **Spring Security** + **Spring Actuator** 

application.properties配置信息:

``` properties
#server
server.port=8080
server.servlet.context-path=/demo
#spring
# 若要访问端点信息，需要配置用户名和密码
spring.security.user.name=admin
spring.security.user.name=123456
#management
# 端点信息接口使用的端口，为了和主系统接口使用的端口进行分离
management.server.port=8090
management.server.servlet.context-path=/sys  #maybe base-path
# 端点健康情况，默认值"never"，设置为"always"可以显示硬盘使用情况和线程情况
management.endpoint.health.showdetails=always
# 设置端点暴露的哪些内容，默认["health","info"]，设置"*"代表暴露所有可访问的端点
management.endpoint.web.exposure.include='*'
```

访问：

http://localhost:8090/sys/actuator/mappings

http://localhost:8090/sys/actuator/beans

## 参考

- actuator文档：https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/reference/htmlsingle/#production-ready
- 具体可以访问哪些路径，参考: https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/reference/htmlsingle/#production-ready-endpoints



## 05. 







## 06. 





## 06. 





## 06. 



## 06. 





## 06. 





## 06. 





## 06. 





## 06. 





## 06. 





## 06. 





## 06. 





