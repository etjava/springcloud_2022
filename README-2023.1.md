# springcloud 完整实例
# 整体架构流程图

# 模块介绍与搭建
springcloud-parent-2023.1 父项目 maven project
- common-2023.1 maven module 公共模块 用来存放公共组件 如实体，全局配置，各工具类等
dependencies
```
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
<!-- 修改后立即生效，热部署 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>springloaded</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
</dependency>
</dependencies>
<build>
    <finalName>common-2023.1</finalName>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```
Teacher实体
```
@Entity
@Table(name="t_teacher")
public class Teacher implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id; // 编号

    @Column(length=50)
    private String name; // 姓名

    @Column(length=50)
    private String subject; // 学科
    // get & set ...
}
```
公共模块创建完成后需要执行install 安装到本地库 方便其它业务模块集成
- provider-1001 服务提供者模块 用来提供服务
dependencies
```
<dependency>
    <groupId>com.etjava</groupId>
    <artifactId>common-2023.1</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
</dependency>
```
application.yml
```
server:
  port: 1001
  context-path: /

# 数据源配置
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://192.168.199.108:3306/db_springcloud
    username: root
    password: Karen@1234
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  thymeleaf:
    cache: false
```
Repository接口 SpringData JPA 对数据库的相关操作
```
package com.etjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.etjava.entity.Teacher;

/**
 * repository接口
 * @author etjav
 *
 */
public interface TeacherRepository extends JpaRepository<Teacher, Integer>,JpaSpecificationExecutor<Teacher>{


```
service接口
```
@Service
public class TeacherServiceImpl implements TeacherService{

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public void save(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    @Override
    public Teacher findById(Integer id) {
        return teacherRepository.findOne(id);
    }

    @Override
    public List<Teacher> list() {
        return teacherRepository.findAll();
    }

    @Override
    public void delete(Integer id) {
        teacherRepository.delete(id);
    }

}

```

Controller 服务提供者对外提供的业务逻辑
```
@RestController
@RequestMapping("/tea")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/list")
    public List<Teacher> list(){
        return teacherService.list();
    }

    @GetMapping("/get/{id}")
    public Teacher get(@PathVariable("id") Integer id) {
        return teacherService.findById(id);
    }

    @PostMapping("/save")
    public boolean save(Teacher teacher) {
        try {
            teacherService.save(teacher);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```
ProviderApp_1001 启动类
```
@SpringBootApplication
public class ProviderApp_1001 {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApp_1001.class, args);
    }
}
```
* 项目启动时会自动创建对应的数据库表

![image](https://user-images.githubusercontent.com/47961027/210161073-77e46b61-3d95-43ad-8230-69195e8b0ac3.png)
* 测试服务提供者

![image](https://user-images.githubusercontent.com/47961027/210161178-13a9a6fc-bcb5-4dcb-bbf7-284bf319c2d9.png)

- consumer-80 服务消费者 用来调用服务
依赖
```
<dependency>
    <groupId>com.etjava</groupId>
    <artifactId>common-2023.1</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
RestTemplate配置 用来请求调用服务提供者提供的服务模板
```
@Configuration
public class SpringCloudConfig {

    /**
     * 调用服务模版
     * @return
     */
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```
Controller 消费者端controller
```
@RestController
@RequestMapping("/tea")
public class CustomerConntroller {

    @Autowired
    private RestTemplate restTemplate;

    private static String PRE_URL="http://localhost:1001";

    @SuppressWarnings("unchecked")
    @GetMapping("/list")
    public List<Teacher> list(){
        return restTemplate.getForObject(PRE_URL+"/tea/list", List.class);
    }

    @GetMapping("/get/{id}")
    public Teacher get(@PathVariable("id") Integer id) {
        return restTemplate.getForObject(PRE_URL+"/tea/get/"+id, Teacher.class);
    }

    @PostMapping("/save")
    public boolean save(@RequestBody Teacher teacher) {
        try {
            restTemplate.postForObject(PRE_URL+"/tea/save", teacher, Boolean.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

```
消费者端启动类
```
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class ConsumerApp_80 {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp_80.class, args);
    }
}
```
测试调用服务提供者提供的消息

![image](https://user-images.githubusercontent.com/47961027/210161684-536d37ce-8bb9-4237-929c-9c5902de1335.png)


此时 一个简单的服务调用基本搭建完成了，但如果想使用服务集群模式 需要借助注册中心，同时为了保证服务的高可用性还需要添加熔断机制等
下面开始服务集群及完善服务的高可用性


## 服务注册与发现
本案例中的服务注册与发现使用Eureka组件实现，源码地址：https://github.com/Netflix/eureka
Eureka是Netflix开发的服务发现框架，本身是一个基于REST的服务，主要用于定位运行在AWS域中的中间层服务，以达到负载均衡和中间层服务故障转移的目的。SpringCloud将它集成在其子项目spring-cloud-netflix中，以实现SpringCloud的服务发现功能
Eureka是Netflix开发的服务发现框架，本身是一个基于REST的服务，主要用于定位运行在AWS域中的中间层服务，以达到负载均衡和中间层服务故障转移的目的。SpringCloud将它集成在其子项目spring-cloud-netflix中，以实现SpringCloud的服务发现功能
Eureka Server提供服务注册服务，各个节点启动后，会在Eureka Server中进行注册，这样EurekaServer中的服务注册表中将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直观的看到。
Eureka Client是一个java客户端，用于简化与Eureka Server的交互，客户端同时也就别一个内置的、使用轮询(round-robin)负载算法的负载均衡器
在应用启动后，将会向Eureka Server发送心跳,默认周期为30秒，如果Eureka Server在多个心跳周期内没有接收到某个节点的心跳，Eureka Server将会从服务注册表中把这个服务节点移除(默认90秒)
Eureka Server之间通过复制的方式完成数据的同步，Eureka还提供了客户端缓存机制，即使所有的Eureka Server都挂掉，客户端依然可以利用缓存中的信息消费其他服务的API。综上，Eureka通过心跳检查、客户端缓存等机制，确保了系统的高可用性、灵活性和可伸缩性
类似zookeeper，Eureka也是一个服务注册和发现组件，是SpringCloud的一个优秀子项目

原理图

![image](https://user-images.githubusercontent.com/47961027/210176346-3e27a8fc-0f34-4523-b63c-0dba9f598f19.png)

# 搭建Eureka服务注册中心
eureka注册中心分为server和client ，eureka自身作为server端 其它都是client ，服务端用来管理所有服务，客户端通过注册中心来调用服务

- eureka-2000 eureka注册中心
添加依赖
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka-server</artifactId>
</dependency>
<!-- 修改后立即生效，热部署 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>springloaded</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
</dependency>
```
application.yml
```
server:
  port: 2000
  context-path: /

eureka:
  instance:
    #eureka注册中心实例名称
    hostname: localhost
  client:
    #false 由于该应用为注册中心，所以设置为false,代表不向注册中心注册自己。
    register-with-eureka: false
    #false 由于注册中心的职责就是维护服务实例，它并不需要去检索服务，所以也设置为false
    fetch-registry: false
    service-url:
       #设置与Eureka注册中心交互的地址，查询服务和注册服务用到  ==> http://localhost:2000/eureka
       defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```
EurekaServerApp_2000 启动类
添加@EnableEurekaServer注解 表示开启eureka server
```
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp_2000 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApp_2000.class, args);
    }
}
```
![image](https://user-images.githubusercontent.com/47961027/210212666-7c00d002-d180-4118-b430-eeae15b4572e.png)

# Eureka注册服务提供者

## 修改provider-1001 将其作为服务提供者注册到eureka注册中心
新增依赖
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```
## application.yml 新增eureka配置
```
eureka:
  instance:
    hostname: localhost  #eureka客户端主机实例名称
    appname: microservice-student  #客户端服务名
    instance-id: microservice-student:1001 #客户端实例名称
    prefer-ip-address: true #显示IP
  client:
    service-url:
      #把服务注册到eureka注册中心  这里的地址需要与eureka注册中心暴漏的地址一致 否则找不到注册中心
      defaultZone: http://localhost:2000/eureka
```
## ProviderApp_1001 启动类添加开启注解
@EnableEurekaClient

## 测试





