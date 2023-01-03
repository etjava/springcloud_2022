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
启动eureka 在启动服务提供者

![image](https://user-images.githubusercontent.com/47961027/210302141-d4e61992-6bf1-4e80-a4bf-57425c709359.png)


## 配置点击示例时提示信息
未配置之前点击示例会出现404 这样是很不友好的，这里我们可以在每个服务提供者中添加当前服务的说明信息
当点击实例时就可以看到具体的服务提供者相关信息了

### 配置方式
springcloud-parent-2023.1 父项目中添加依赖
```
<!-- 构建的时候 解析 src/main/resources 下的配置文件 其实就是application.yml 解析以$开头和结尾的信息 -->
<build>
    <finalName>springcloud-parent-2023.1</finalName>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>true</filtering>
        </resource>
    </resources>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-resources-plugin</artifactId>
            <configuration>
                <delimiters>
                    <delimit>$</delimit>
                </delimiters>
            </configuration>
        </plugin>
    </plugins>
</build>
```
provider-1001 服务提供者添加依赖
```
<!-- actuator监控引入 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
服务提供者的配置文件添加项目描述信息
application.yml
```
info:
   groupId: $project.groupId$
   artifactId: $project.artifactId$
   version: $project.version$
   负责人: Tom
   联系电话: 123456
```
![image](https://user-images.githubusercontent.com/47961027/210302212-81524f70-e244-40c3-af2a-f9cb79f26fe8.png)


![image](https://user-images.githubusercontent.com/47961027/210302177-133a30cf-00a8-4cad-b14d-3f77b717af8c.png)

# eureka高可用集群搭建
当面对高并发时需要采用集群方式应对 (注册中心集群，服务提供者集群)
其本质是三个服务注册中心都有我们服务提供者的信息，服务调用时通过一些策略（默认轮询），会去找对应的服务注册中心；通过集群，能减轻每个服务注册中心的压力；
## 新建两个eureka server
### eureka-2001
依赖
```
<dependencies>
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
</dependencies>
```
eureka-2001启动类
```
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp_2001 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApp_2001.class, args);
    }
}
```
eureka-2001 配置文件
```
server:
  port: 2001
  context-path: /

eureka:
  instance:
    # 单机 hostname: localhost #eureka注册中心实例名称
    hostname: eureka2001.etjava.com # 集群
  client:
    #false 由于该应用为注册中心，所以设置为false,代表不向注册中心注册自己。
    register-with-eureka: false
    #false 由于注册中心的职责就是维护服务实例，它并不需要去检索服务，所以也设置为false
    fetch-registry: false
    service-url:
      # 集群
      defaultZone: http://eureka2000.etjava.com:2000/eureka/,http://eureka2002.etjava.com:2002/eureka/
      #单机defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/       #设置与Eureka注册中心交互的地址，查询服务和注册服务用到
```
### eureka-2002
eureka-2002 依赖
```
<dependencies>
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
</dependencies>
```
eureka-2002 启动类
```
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp_2002 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApp_2002.class, args);
    }
}
```
eureka-2002 application.yml
```
server:
  port: 2002
  context-path: /

eureka:
  instance:
    # 单机 hostname: localhost #eureka注册中心实例名称
    hostname: eureka2002.etjava.com # 集群
  client:
    #false 由于该应用为注册中心，所以设置为false,代表不向注册中心注册自己。
    register-with-eureka: false
    #false 由于注册中心的职责就是维护服务实例，它并不需要去检索服务，所以也设置为false
    fetch-registry: false
    service-url:
      # 集群
      defaultZone: http://eureka2000.etjava.com:2000/eureka/,http://eureka2001.etjava.com:2001/eureka/
      #单机defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/       #设置与Eureka注册中心交互的地址，查询服务和注册服务用到
```
### eureka-2000
eureka-2000 配置文件修改
添加注册集群地址 注意不能自己注册自己
```
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
       # defaultZone:  http://${eureka.instance.hostname}:${server.port}/eureka/
       defaultZone: http://eureka2001.etjava.com:2001/eureka/,http://eureka2002.etjava.com:2002/eureka/
```
### provider-1001 服务提供者
修改defaultZone为集群模式
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

eureka:
  instance:
    hostname: localhost  #eureka客户端主机实例名称
    appname: microservice-student  #客户端服务名
    instance-id: microservice-student:1001 #客户端实例名称
    prefer-ip-address: true #显示IP
  client:
    service-url:
      #把服务注册到eureka注册中心  这里的地址需要与eureka注册中心暴漏的地址一致 否则找不到注册中心
#      defaultZone: http://localhost:2000/eureka
# 单机    defaultZone: http://localhost:2001/eureka   #把服务注册到eureka注册中心
     # 集群
      defaultZone: http://eureka2001.etjava.com:2001/eureka/,
                   http://eureka2002.etjava.com:2002/eureka/,
                   http://eureka2000.etjava.com:2000/eureka/



info:
   groupId: $project.groupId$
   artifactId: $project.artifactId$
   version: $project.version$
   负责人: Tom
   联系电话: 123456
```
### 启动测试
首先启动三个eureka server ,然后在启动provider
![image](https://user-images.githubusercontent.com/47961027/210362973-08cebf80-638e-48b2-8d52-9092cc2f004b.png)

# Ribbon 简介
Ribbon是Netflix发布的负载均衡器，它有助于控制HTTP和TCP的客户端的行为。为Ribbon配置服务提供者地址后，Ribbon就可基于某种负载均衡算法，自动地帮助服务消费者去请求。Ribbon默认为我们提供了很多负载均衡算法，例如轮询、随机等。当然，我们也可为Ribbon实现自定义的负载均衡算法。
在Spring Cloud中，当Ribbon与Eureka配合使用时，Ribbon可自动从Eureka Server获取服务提供者地址列表，并基于负载均衡算法，请求其中一个服务提供者实例 如下图
![image](https://user-images.githubusercontent.com/47961027/210363774-bf4531a6-f899-4202-a319-14d84f2d30a8.png)

# Ribbon 基本应用
Ribbon是客户端负载均衡，所以肯定集成在消费端，也就是consumer端
## 修改consumer-80
添加Ribbon依赖
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-ribbon</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```
修改application.yml
```
eureka:
  client:
    #false 由于注册中心的职责就是维护服务实例，它并不需要去检索服务，所以也设置为false
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka2001.etjava.com:2001/eureka/,
                   http://eureka2002.etjava.com:2002/eureka/,
                   http://eureka2000.etjava.com:2000/eureka/
```
SpringCloudConfig添加负载均衡配置
即 在调用服务的RestTemplate上添加@LoadBalanced注解即可
```
/**
 * SpringCloud相关配置
 * @author Administrator
 *
 */
@Configuration
public class SpringCloudConfig {

    /**
     * 调用服务模版
     * @return
     */
    @Bean
    @LoadBalanced  // 引入ribbon负载均衡
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```
启动类需要添加上@EnableEurekaClient注解
```
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableEurekaClient
public class ConsumerApp_80 {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp_80.class, args);
    }
}
```
修改provider的application.yml
指定当前服务的名称 因为consumer调用时需要通过服务名称进行调用
```
# 数据源配置
spring:
  application:
  # 服务名称 消费者需要通过该名称进行调用服务
    name: provider-1001
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
controller调用服务的PRE_URL改为服务名称
```
private static String PRE_URL="http://provider-1001";
```
测试
启动三个eureka注册中心，然后启动provider 最后启动consumer进行服务调用
![image](https://user-images.githubusercontent.com/47961027/210366634-9fc9f667-7116-4939-8fdc-6fa4ca4e4e9f.png)
# Ribbon 负载均衡
上述demo还没实现真正负载均衡，这里要在搞两个服务提供者并搞成集群(已有provider-1001)，然后才能演示负载均衡，以及负载均衡策略；
## provider-1002，provider-1003
依赖，application.yml,业务及启动类均复制provider-1001代码 然后将application.yml修改下端口及instance实例名称即可
provider-1002
instance-id: microservice-student:1002
provider-1003
instance-id: microservice-student:1003
做完上述修改后需要在服务提供者的controller中添加打印语句或getInfo方法 方便查看是哪个服务提供者提供的服务
```
@GetMapping("/getInfo")
public Map<String,Object> getInfo() {
    Map<String,Object> map = new HashMap<>();
    map.put("info",1001);
    return map;
}
```
在consumer的controller中添加对应的服务调用方法
```
@GetMapping("/getInfo")
public Map<String,Object> getInfo(){
    return restTemplate.getForObject(PRE_URL+"/tea/getInfo", Map.class);
}
```
测试Ribbon的默认方式(默认轮询)
启动三个eureka,三个provider,consumer进行访问
![70d08f5cdbe9ebc1659c88b6c8f30c7](https://user-images.githubusercontent.com/47961027/210400792-5297bcb4-3629-4f63-b3f9-64ca0928a62d.png)
![1f9c2a18ece068217812d0f6e11b47d](https://user-images.githubusercontent.com/47961027/210400807-3d28bfc5-6d69-4600-a429-ea3facbd9d14.png)



测试负载均衡
修改consumer的SpringCloudConfig
添加IRule

```
 /**
 * 自定义轮询算法
 * RetryRule 重试机制
 * 在一个配置时间段内当选择server不成功，则一直尝试使用subRule的方式选择一个可用的server
 * @return
 */
@Bean
public IRule myRule(){
    return new RetryRule();
}
```
Ribbon负载均衡策略

| 策略名                    | 策略声明                                                     | 策略描述                                                     | 实现说明                                                     |
| ------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| BestAvailableRule         | public class BestAvailableRule extends ClientConfigEnabledRoundRobinRule | 选择一个最小的并发请求的server                               | 逐个考察Server，如果Server被tripped了，则忽略，在选择其中ActiveRequestsCount最小的server |
| AvailabilityFilteringRule | public class AvailabilityFilteringRule extends PredicateBasedRule | 过滤掉那些因为一直连接失败的被标记为circuit tripped的后端server，并过滤掉那些高并发的的后端server（active connections 超过配置的阈值） | 使用一个AvailabilityPredicate来包含过滤server的逻辑，其实就就是检查status里记录的各个server的运行状态 |
| WeightedResponseTimeRule  | public class WeightedResponseTimeRule extends RoundRobinRule | 根据响应时间分配一个weight，响应时间越长，weight越小，被选中的可能性越低 | 一个后台线程定期的从status里面读取评价响应时间，为每个server计算一个weight。Weight的计算也比较简单responsetime 减去每个server自己平均的responsetime是server的权重。当刚开始运行，没有形成status时，使用roubine策略选择server。 |
| RetryRule                 | public class RetryRule extends AbstractLoadBalancerRule      | 对选定的负载均衡策略机上重试机制                             | 在一个配置时间段内当选择server不成功，则一直尝试使用subRule的方式选择一个可用的server |
| RoundRobinRule            | public class RoundRobinRule extends AbstractLoadBalancerRule | roundRobin方式轮询选择server                                 | 轮询index，选择index对应位置的server                         |
| RandomRule                | public class RandomRule extends AbstractLoadBalancerRule     | 随机选择一个server                                           | 在index上随机，选择index对应位置的server                     |
| ZoneAvoidanceRule         | public class ZoneAvoidanceRule extends PredicateBasedRule    | 复合判断server所在区域的性能和server的可用性选择server       | 使用ZoneAvoidancePredicate和AvailabilityPredicate来判断是否选择某个server，前一个判断判定一个zone的运行性能是否可用，剔除不可用的zone（的所有server），AvailabilityPredicate用于过滤掉连接数过多的Server |

# Feign简介
Feign是一个声明式的Web Service客户端，它使得编写Web Serivce客户端变得更加简单。我们只需要使用Feign来创建一个接口并用注解来配置它既可完成。它具备可插拔的注解支持，包括Feign注解和JAX-RS注解。Feign也支持可插拔的编码器和解码器。Spring Cloud为Feign增加了对Spring MVC注解的支持，还整合了Ribbon和Eureka来提供均衡负载的HTTP客户端实现
前面Ribbon调用服务提供者，我们通过restTemplate调用，缺点是，多个地方调用，同一个请求要写多次，不方便统一维护，这时候Feign来了，就直接把请求统一搞一个service作为FeignClient，然后其他调用Controller需要用到的，直接注入service，直接调用service方法即可；同时Feign整合了Ribbon和Eureka，所以要配置负载均衡的话，直接配置Ribbon即可，无其他特殊地方；当然Fiegn也整合了服务容错保护，断路器Hystrix
简单理解 Fiegn就是封装了Ribbon同时又对SpringMVC进行了封装 我们在调用微服务时可以像SpringMVC那样去编写service调用业务层

## Feign应用

首先在common模块里建一个service（实际项目肯定是多个service）作为Feign客户端，用Feign客户端来调用服务提供者，当然可以配置负载均衡；Feign客户端定义的目的，就是为了方便给其他项目调用；
### common-2023.1

添加依赖

```
<!-- Feign依赖 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-feign</artifactId>
</dependency>
```
新建FeignClientService接口
该接口必须使用@FeignClient``(value=``"provider-1001")注解 同时指定需要调用的服务名称
接口中方法借鉴provider服务提供者

```
@FeignClient(value="provider-1001")
public interface FeignClientService {

    /**
     * 添加或者修改老师信息
     * @param Teacher
     */
    @PostMapping(value="/tea/save")
    public void save(Teacher teacher);

    /**
     * 根据id查找老师信息
     * @param id
     * @return
     */
    @GetMapping(value="/tea/findById/{id}")
    public Teacher findById(@PathVariable("id") Integer id);

    /**
     * 查询老师信息
     * @return
     */
    @GetMapping(value="/tea/list")
    public List<Teacher> list();

    /**
     * 根据id删除老师信息
     * @param id
     */
    @GetMapping(value="/tea/delete/{id}")
    public void delete(@PathVariable("id") Integer id);

    /**
     * 获取信息
     * @return
     */
    @GetMapping(value="/tea/getInfo")
    public Map<String,Object> getInfo();
    @GetMapping(value="/tea/get/{id}")
    public Teacher get(Integer id) ;
}

```
公共模块修改完成后需要重新install 方便其它模块调用

### 新建消费者 consumer-feign-80
添加依赖
```
<dependencies>
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
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-eureka</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-ribbon</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-config</artifactId>
	</dependency>
        <!-- Feign依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-feign</artifactId>
    </dependency>
</dependencies>
```
创建application.yml配置文件
```
server:
  port: 80
  context-path: /

eureka:
  client:
    #false 由于注册中心的职责就是维护服务实例，它并不需要去检索服务，所以也设置为false
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka2001.etjava.com:2001/eureka/,
                   http://eureka2002.etjava.com:2002/eureka/,
                   http://eureka2000.etjava.com:2000/eureka/

```
创建Controller
因为现在用Fiegn，所以把restTemplate去掉，改成注入service，调用service方法来实现服务的调用；
```
package com.etjava.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etjava.entity.Teacher;
import com.etjava.service.FeignClientService;

@RestController
@RequestMapping("/tea")
public class CustomerConntroller {


    @Autowired
    private FeignClientService feignClientService;


    @GetMapping("/list")
    public List<Teacher> list(){
        return feignClientService.list();
    }

    @GetMapping("/getInfo")
    public Map<String,Object> getInfo(){
        return feignClientService.getInfo();
    }

    @GetMapping("/get/{id}")
    public Teacher get(@PathVariable("id") Integer id) {
        return feignClientService.get(id);
    }

    @PostMapping("/save")
    public boolean save(@RequestBody Teacher teacher) {
        try {
            feignClientService.save(teacher);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

```
启动类
启动类需要添加@EnableFeignClients注解开启Feign  否则@FeignClient无法注入
```
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableEurekaClient
@EnableFeignClients
public class ConsumerFeignApp_80 {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerFeignApp_80.class, args);
    }
}
```
测试
启动三个eureka,三个provider,最后启动带有feign的consumer进行调用
![image](https://user-images.githubusercontent.com/47961027/210408570-9a195b29-d715-4c3e-9983-8824c7accb0b.png)
![image](https://user-images.githubusercontent.com/47961027/210408593-84b67ad5-3d27-4464-b858-6d0ba7fbd601.png)
测试Ribbon的负载均衡
通过修改consumer-feign-80模块中的SpringCloudConfig.myRule 测试不同的负载均衡

# Hystrix 断路器
在微服务系统中 服务调用失败是不可避免的，例如网络超时，服务自身出现异常等，那么要怎么保证在一个服务出现异常时 不会导致整体服务失败呢，这个就是Hystrix需要做的事情
Hystrix提供了熔断，隔离，Fallback，Cache，监控等功能，能够在一个或多个服务出现异常时保证系统依然可用
Hystrix可以使用在服务提供者中，也可以使用在服务消费者中
在整合Feign之前 添加在服务提供者中 当去调用服务提供者的方法时 如方法出现超时则会进行熔断降级
## 服务的雪崩效应
正常访问如下图
![image](https://user-images.githubusercontent.com/47961027/210410671-98db6175-4a9e-44a5-8ebf-095e9971d2e6.png)
但是当请求的服务出现异常时 其它用户的请求将被阻塞 如下图
![image](https://user-images.githubusercontent.com/47961027/210410836-4873253b-5213-4078-8bb7-d743082b2645.png)
如果多个用户请求一直处于阻塞状态就会导致整体服务不可使用 这种情况就是服务雪崩
![image](https://user-images.githubusercontent.com/47961027/210410980-2611cac1-5411-46a0-825e-979933525d30.png)
## 解决雪崩效应
Hystrix服务熔断降级 @HystrixCommand fallbackMethod
熔断机制是应对服务雪崩效应的一种微服务链路保护机制，当某个服务不可用或响应超时 会进行服务降级处理，进而熔断该节点的服务调用 快速返回自定义错误信息
## 创建带有熔断机制的provider
provider-hystrix-1004
添加依赖
```
  <dependencies>
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
    <!-- eureka注册中心依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>

    <!-- actuator监控引入 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- hystrix熔断相关依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-hystrix</artifactId>
    </dependency>
</dependencies>
```
application.yml
```
server:
  port: 1004
  context-path: /

# 数据源配置
spring:
  application:
  # 服务名称 消费者需要通过该名称进行调用服务
    name: provider-1004
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

eureka:
  instance:
    hostname: localhost  #eureka客户端主机实例名称
    appname: provider-1001  #客户端服务名
    instance-id: microservice-student:1004 #客户端实例名称
    prefer-ip-address: true #显示IP
  client:
    service-url:
      #把服务注册到eureka注册中心  这里的地址需要与eureka注册中心暴漏的地址一致 否则找不到注册中心
#      defaultZone: http://localhost:2000/eureka
# 单机    defaultZone: http://localhost:2001/eureka   #把服务注册到eureka注册中心
     # 集群
      defaultZone: http://eureka2001.etjava.com:2001/eureka/,
                   http://eureka2002.etjava.com:2002/eureka/,
                   http://eureka2000.etjava.com:2000/eureka/



info:
   groupId: $project.groupId$
   artifactId: $project.artifactId$
   version: $project.version$
   负责人: Hystrix
   联系电话: 123456
```
启动类
启动类需要添加@EnableCircuitBreaker 注解开启熔断机制
```
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker // 开启熔断
public class ProviderApp_1004 {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApp_1004.class, args);
    }
}
```
controller,repository,service 直接在其它provider中copy一份即可
然后controller中新增validHystrix方法
```
 /**
 * 测试验证带有熔断机制服务
 * @return
 * @throws InterruptedException
 */
@GetMapping(value="/validHystrix")
@HystrixCommand(fallbackMethod="getInfoFallback")
public Map<String,Object> validHystrix() throws InterruptedException{
    Thread.sleep(2000);
    Map<String,Object> map=new HashMap<String,Object>();
    map.put("code", 200);
    map.put("info", "业务数据-----1004");
    return map;
}

public Map<String,Object> getInfoFallback() throws InterruptedException{
    Map<String,Object> map=new HashMap<String,Object>();
    map.put("code", 500);
    map.put("info", "系统出错，稍后重试");
    return map;
}
```
创建测试hystrix的consumer
consumer-hystrix-80
添加依赖
```
  <dependencies>
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
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-ribbon</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
</dependencies>
```
配置RestTemplate
```
/**
 * SpringCloud相关配置
 * @author Administrator
 *
 */
@Configuration
public class SpringCloudConfig {

    /**
     * 调用服务模版
     * @return
     */
    @Bean
    @LoadBalanced  // 引入ribbon负载均衡
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
    /**
     * 自定义轮询算法
     * @return
     */
    @Bean
    public IRule myRule(){
        return new RetryRule();
    }
}
```
controller中添加验证hystrix熔断机制方法
```
@RestController
@RequestMapping("/tea")
public class CustomerConntroller {

    @Autowired
    private RestTemplate restTemplate;

    private static String PRE_URL="http://provider-1004";

    @SuppressWarnings("unchecked")
    @GetMapping(value="/validHystrix")
    public Map<String,Object> validHystrix() throws InterruptedException{
       return restTemplate.getForObject(PRE_URL+"/tea/validHystrix", Map.class);
    }
}
```
测试
启动三个eureka,provider-hystrix-1004服务提供者，最后启动consumer-hystrix-80进行测试

![image](https://user-images.githubusercontent.com/47961027/210416308-b24ad346-bee9-4f68-8b8b-60453563d53c.png)

![image](https://user-images.githubusercontent.com/47961027/210416413-fc5a55db-224a-465a-ac79-c155323e77bb.png)

## Hystrix默认超时时间设置
hystrix-core.jar com.netflix.hystrix包下的HystrixCommandProperties类
default_executionTimeoutInMilliseconds属性默认的超时时间 默认1s
可以通过在application.yml中修改其默认超时时间
修改provider-hystrix-1004
application.yml
添加hystrix超时时间配置
```
# hystrix 超时时间配置
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 3000
```
然后再次测试 如下

