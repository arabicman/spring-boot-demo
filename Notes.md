# Spring Boot练习笔记

## 01.项目搭建

(1)创建一个maven空项目命名为spring-boot-demo，作为parent。

(2)新建一个module，并选择spring-boot-demo为parent.

(3)【重要:每次必做】使用module中的pom.xml文件用作模板，配置后续使用Spring Initializr新建module中的pom.xml

	*  复制<parent></parent>标签中的内容
	*  删除<groupId></groupId>标签
	*  <verision></version>可以删除或与parent保持一致
	*  在parent的pom.xml中<modules></modules>中添加module

(4)之后就可以使用Spring Initializr创建module了。



## 02. Hello World
(1) Spring Initializr: 选**Spring Web**。

(2) 按照01.(3)步骤修改pom.xml 并导入依赖。

(3) 修改application.properties(或者application.yml)。

(4) 写一个简单的HelloController在com.demo.demo02.controller包下。

(5) 运行SpringBootApplication并启用SpringBootRunDashboard（IntelliJ IDE）。



## 03. 从Properties中读取数据

Initializr: **Spring Web** + **Configuration Processor**

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



## 04. Spring Boot Actuator 的使用

(1) Initializr: **Spring Web** + **Spring Security** + **Spring Actuator** 

(2) application.properties配置信息:

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
management.server.servlet.base-path=/sys  #context-path过期了
# 端点健康情况，默认值"never"，设置为"always"可以显示硬盘使用情况和线程情况
management.endpoint.health.showdetails=always
# 设置端点暴露的哪些内容，默认["health","info"]，设置"*"代表暴露所有可访问的端点
management.endpoints.web.exposure.include='*'
```

(3) 启动并访问：

http://localhost:8090/sys/actuator/mappings

http://localhost:8090/sys/actuator/beans

(4) 参考:

- actuator文档：https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/reference/htmlsingle/#production-ready
- 具体可以访问哪些路径，参考: https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/reference/htmlsingle/#production-ready-endpoints



## 05. Spring Boot Admin Client的使用

(1) Initializr: **Spring Web** + **Spring Security** + **Admin Client ** + **Actuator**

(2) application.properties

``` properties
#server
server.port=8080
server.servlet.context-path=/demo
#spring
spring.application.name=spring-boot-demo-admin-client
spring.security.user.name=admin
spring.security.user.password=123456
# Spring Boot Admin 服务端地址（yml加双引号“”）
spring.boot.admin.client.url=http://localhost:8000/
# 客户端端点信息的安全认证信息
spring.boot.admin.client.username=${spring.security.user.name}
spring.boot.admin.client.password=${spring.security.user.password}
spring.boot.admin.client.instance.metadate.user.name=${spring.security.user.name}
spring.boot.admin.client.instance.metadate.user.password=${spring.security.user.password}
#management
# 端点健康情况，默认值"never"，设置为"always"可以显示硬盘使用情况和线程情况
management.endpoint.health.show-details=always
# 设置端点暴露的哪些内容，默认["health","info"]，设置"*"代表暴露所有可访问的端点（yml加双引号“”）
management.endpoints.web.exposure.include=*
```

(3)写一个简单的IndexController

``` java
@RestController
public class IndexController {
    @GetMapping(value = {"", "/"})
    public String index() {
        return "This is a Spring Boot Admin Client.";
    }
}
```



## 06. Spring Boot Admin Server的使用

(1) Initializr: **Spring Web** + **Spring Security** + **Admin Server**

(2) application.properties

```properties
server.port=8000
#spring
spring.application.name=spring-boot-demo-admin-server
#如果不配置就使用user和控制台生成的密码
spring.security.user.name=admin
spring.security.user.password=123456
```

(3) 添加@EnableAdminServer注解

(4) 【非必要】解决The request was rejected because the URL contained a potentially malicious String "//"

``` java
//放入SpringApplication即可
@Bean
    public HttpFirewall allowUrlSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
      	firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }
```

(5) 添加Config配置类

``` java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler
                = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl("/");

        http.authorizeRequests()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/login")
                .successHandler(successHandler).and()
                .logout().logoutUrl("/logout").and()
                .httpBasic().and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers(
                        "/instances",
                        "/actuator/**"
                );
    }
}
```

详细参考： https://www.jianshu.com/p/1749f04105fb



## 07. 配置Logback

(1) Initializr: **Web** + **Lombok**

(2) 配置application.yml 以及 logback-spring.xml（下面仅为基本结构，参考源代码为主）

```xml
<configuration scan="true" scanPeriod="60 seconds" debug="false">  
    <property name="glmapper-name" value="glmapper-demo" /> 
    <contextName>${glmapper-name}</contextName> 
        
    <appender>
       <!--xxxx具体功能-->
    </appender>   
    
    <logger>
        <!--引用appender-->
    </logger>
    
    <root>             
       <!--引用appender或者logger-->
    </root>  
</configuration> 
```

(3)SpringBootApplication类

``` java
@SpringBootApplication
@Slf4j
public class SpringBootDemoLogbackApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringBootDemoLogbackApplication.class, args);
		int length = context.getBeanDefinitionNames().length;
		log.trace("Spring boot启动初始化了 {} 个 Bean", length);
		log.debug("Spring boot启动初始化了 {} 个 Bean", length);
		log.info("Spring boot启动初始化了 {} 个 Bean", length);
		log.warn("Spring boot启动初始化了 {} 个 Bean", length);
		log.error("Spring boot启动初始化了 {} 个 Bean", length);
		try {
			int i = 0;
			int j = 1 / i;
		} catch (Exception e) {
			log.error("【SpringBootDemoLogbackApplication】启动异常：", e);
		}
	}
}
```



## 08. Spring Boot AOP日志

(1) Initializr: Spring WEB + Spring AOP + Lombok + Hutools + UserAgentUtils。

(2) 复制07.里面的application.properties 和 logback-spring.xml并粘贴到resources中。

(3) 创建aspectj package 并在其中创建AopLog.java

``` java
@Aspect
@Component
@Slf4j
public class AopLog {
    @Pointcut("execution(public * com.demo.logaop.controller.*Controller.*(..))") 
    public void log() {  }

    @Around("log()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {

        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        // 打印请求相关参数
        long startTime = System.currentTimeMillis();
        Object result = point.proceed();
        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);

        final Log l = Log.builder()
                .threadId(Long.toString(Thread.currentThread().getId()))
                .threadName(Thread.currentThread().getName())
                .ip(getIp(request))
                .url(request.getRequestURL().toString())
                .classMethod(String.format("%s.%s", point.getSignature().getDeclaringTypeName(),
                        point.getSignature().getName()))
                .httpMethod(request.getMethod())
                .requestParams(getNameAndValue(point))
                .result(result)
                .timeCost(System.currentTimeMillis() - startTime)
                .userAgent(header)
                .browser(userAgent.getBrowser().toString())
                .os(userAgent.getOperatingSystem().toString()).build();

        log.info("Request Log Info : {}", JSONUtil.toJsonStr(l));

        return result;
    }
  //未完....
}
```



(4) 创建controller package并创建TestController.java

(5) 使用postman测试api	

```json
Step1: GET  http://localhost:8080/demo/test?who=xxxxxxx

Step2: POST http://localhost:8080/demo/testJson
BODY-JSON:
          { 
              "data" : 
              { 
                  "field1" : "value1", 
                  "field2" : "value2"
              }
          }
```



## 09. Exception Handler异常统一处理

两种方法：第一种对常见API形式的接口进行异常处理，统一封装返回格式；第二种是对模板页面请求的异常处理，统一处理错误页面。

(1) Initializr: Spring Web + Thymeleaf + lombok

(2) application.yml

``` yml
server:
  port: 8080
  servlet:
    context-path: /demo
spring:
  thymeleaf:
    cache: false
    suffix: .html
    mode: HTML
    encoding: UTF-8
    servlet:
      content-type: text/html
```

(3) 创建error.htm并将Thymeleaf标签加到html中

``` html
<html xmlns:th="http://www.thymeleaf.org"> 
```

(4) 创建Status, ApiResponse, BaseException, JsonException, PageException类

(5) 创建DemoExceptionHandler

``` java
@ControllerAdvice
@Slf4j
public class DemoExceptionHandler {
    private static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = JsonException.class)
    @ResponseBody
    public ApiResponse jsonErrorHandler(JsonException exception){
        log.error("【JsonException】:{}", exception.getMessage());
        return ApiResponse.ofException(exception);
    }

    @ExceptionHandler(value = PageException.class)
    public ModelAndView pageErrorHandler(PageException exception){
        log.error("【DemoPageException】:{}", exception.getMessage());
        ModelAndView view = new ModelAndView();
        view.addObject("message", exception.getMessage());
        view.setViewName(DEFAULT_ERROR_VIEW);
        return view;
    }
}
```

(6) 写Controller并运行

## 10. Thymeleaf模板

(1) Initializr : web + thymeleaf + lombok

(2)application.yml

``` yml
server:
  port: 8080
  servlet:
    context-path: /demo
spring:
  thymeleaf:
    mode: HTML
    encoding: UTF-8
    servlet:
      content-type: text/html
    cache: false
```

(3) 创建User实体类

(4) 创建两个controller： IndexController 和 UserController

(5) 把<thymeleaf>标签写入html中

``` html
<form action="/demo/user/login" method="post">
		用户名<input type="text" name="name" placeholder="用户名"/>
		密码<input type="password" name="password" placeholder="密码"/>
		<input type="submit" value="登录">
	</form>
```



thymeleaf: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html  

## 11. Spring Data JPA

(1) Initializr: Web + data jpa + mysql driver

(2) application.properties

``` properties
#server
server.port=8080
server.servlet.context-path=/demo
#data source
spring.datasource.url= jdbc:mysql://localhost:3307/demodb?useSSL=false
spring.datasource.username= root
spring.datasource.password= 123456
#jpa
spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.show-sql= true
# Hibernate ddl auto (create, create-drop, 开发可用validate（帮助创建表）, 产品常用update)
spring.jpa.hibernate.ddl-auto= update
```

(3) 创建实体类Tutorial

(4) 创建TutorialRepository interface 继承 JpaRepository<T,R>

``` java
public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    //已有实现方法：save(), getOne(), findById(), findAll(), count(), delete(), deleteById()
    List<Tutorial> findByPublished(boolean published);
    List<Tutorial> findByTitleContaining(String title);
}
```

(5) 创建TutorialController(增删改查API)



## 12. 





## 13. 





## 14. 





## 15. 





## 16. 





