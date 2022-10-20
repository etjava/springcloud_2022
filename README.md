# Spring Cloud 练习 DEMO
## Module description

### springcloud_student_parent 所有模块的父模块 用来管理其他module,依赖及其他相关信息(例如 各个模块之间的编译版本,统一编码等)

### springcloud_student_common 公共模块 用来存放JavaBean,各种工具类等

### springcloud_student_provider 服务提供者模块 生产消息发布到注册中心
### springcloud_student_provider_1002 服务提供者模块 这里是用来模拟多台服务提供者
### springcloud_student_provider_1003 服务提供者 这里用来模拟多台服务提供者
-- 服务提供者在接收对象参数时需要使用@RequestBody标注 例如 (@RequestBody Student student)


### springcloud_student-consumer 服务消费者 远程注册中心的服务
-- 通过RestTemplate进行对远程服务调用

## Eureka1.x 服务注册中心搭建
### springcloud_etudent_eureka_2000 eureka注册中心(单个)
### 集群版的Eureka注册中心搭建
#### springcloud_etudent_eureka_2000 eureka注册中心1
#### springcloud_etudent_eureka_2002 eureka注册中心2
#### springcloud_etudent_eureka_2003 eureka注册中心3






# 分支介绍
## 2022分支 从基础搭建到Hystrix熔断降级
## 2022.1 分支 Hystrix,feign 整合使用 实现彻底解耦
# SPRINGCLOUD DEMO

# 项目介绍
-----------------------------------------------------------------------------
## springcloud_student_parent 所有项目的父项目
## springcloud_student_common 所有子模块的公共包
## springcloud_etudent_eureka_2000 Eureka注册中心
## springcloud_student_eureka_2002 Eureka注册中心
## springcloud_student_eureka_2003 Eureka注册中心
## springcloud_student_provider 服务提供者
## springcloud_student_provider_1002 服务提供者
## springcloud_student_provider_1003 服务提供者
## springcloud_student-consumer 服务消费者
## springcloud_student_consumer_feign_80 带有feign的消费服务者
## springcloud_student_provider_hystrix_1004 带有熔断机制的服务提供者
## springcloud_student_provider_hystrix_1005 带有熔断机制的服务提供者
## springcloud_student_consumer_hystrix_dashborad_90 Hystrix的监控平台 图形化和窗口化都有
## springcloud_student_consumer_hystrix_turbine_91 带有熔断机制采用turbine的服务消费者
## springcloud_student_zuul_3001 zuul路由 虽然不是服务提供者也不是服务消费者 但需要注册中Eureka中
## springcloud_config_server_6001 springcloud config server端 用来读取远程库中的配置文件
## springcloud_config_client springcloud-config client端 正常开发中这个需要融合到服务消费者端
## springcloud_student_eureka_server_config_2004 springcloud-config与Eureka整合
## springcloud_student_provider_config_1004 带有springcloud-config的服务提供者
## springcloud_student_consumer_config_81 带有springcloud-config的服务消费者

-----------------------------------------------------------------------------
# springcloud_student_parent
## 是所有项目的父项目，用来统一管理其它子模块中的依赖，编码等

# springcloud_student_common
## 是所有子模块的公共包，里面封装了JavaBean,Service接口,常用工具类等

# springcloud_etudent_eureka_2000
## 是注册中心 用来发现与注册消息服务的
### 采用集群是需要注意 不能自己向自己注册
```
server:
  port: 2001
  context-path: /

eureka:
  instance:
    # hostname: localhost 单机eureka注册中心实例名称 - 访问地址
    hostname: eureka2001.etjava.com #eureka注册中心实例名称 - 访问地址
  client:
    register-with-eureka: false     #false 由于该应用为注册中心，所以设置为false,代表不向注册中心注册自己。
    fetch-registry: false     #false 由于注册中心的职责就是维护服务实例，它并不需要去检索自己的服务，所以也设置为false(不需要检查自己是否存活状态)
    service-url:
                    # 单机 设置与Eureka注册中心交互的地址，查询服务和注册服务用到(对外提供服务的访问地址)
       # defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
                   # 集群 多个注册中心使用逗号隔开  注意 不能注册到自己
       defaultZone: http://eureka2002.etjava.com:2002/eureka/,http://eureka2003.etjava.com:2003/eureka/
```

# springcloud_student-consumer
## 服务的消费者 ，用来调用注册中心的服务
```
server:
  port: 80
  context-path: /

# ribbon 相关配置
eureka:
  client:
    #register-with-eureka: false  由于注册中心的职责就是维护服务实例，它并不需要去检索服务，所以设置为false
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka2001.etjava.com:2001/eureka/,
                    http://eureka2002.etjava.com:2002/eureka/,
                    http://eureka2003.etjava.com:2003/eureka/


# 单机 需要指定服务提供者的IP+端口
# provider_host: http://localhost:1001

# 集群版 STUDENT-PROVIDER22 对应的是服务提供者配置文件中的application.name的值
# 这里只能使用中划线 不能使用下划线 提供者那边也只能使用中划线
provider_host: http://STUDENT-PROVIDER
```
### 调用注册中心服务时有两种方式
#### 1 RestTemplate 该方式底层采用http请求 可以直接将其作为Bean注入到Spring中 在controller中直接引用即可
#### 2 Ribbon 该方式采用负载均衡机制进行调用（底层采用的是各种算法的轮询及随机）
```
@Configuration
public class SpringCloudConfig {

    /**
     *  调用服务模版
     *  通过RestTemplate 调用远程服务
     * @return
     */
    @Bean
    @LoadBalanced  // 引入ribbon负载均衡
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    /**
     *  自定义轮询算法 默认5次轮询 5次访问仍热失败则不在去访问失败的节点
     * @return
     */
    @Bean
    public IRule myRule(){
        return new RetryRule();
    }
}
```
# springcloud_student_consumer_feign_80
## 底层封装了Ribbon 同时Feign封装了SpringMVC ，我们可以使用SpringMVC模式来实现功能
### SpringMVC - 控制层调用业务层，业务层调用持久层
### 如果是单独使用Ribbon 业务层就会融合到控制层 不利于系统维护

# springcloud_student_provider_hystrix_1004
## 带有熔断和服务降级机制的服务提供者
### hystrix是springcloud的一种自我保护机制
### 当网络异常或请求超时时 hystrix会将请求直接过滤掉 采用服务降级后的对应的方法进行返回 而不是一直等待系统响应
### Dashborad 是hystrix的监控平台，在该平台可以看到默认的10秒内的所有请求及hystrix所做的处理
### Turbine 是用来监测hystrix处理集群性质的服务调用  turbine是基于Dashborad 实际操作还是Dashborad
### Fegin与Hystrix整合后就可以直接使用MVC模式开发项目，同时还可以通过Dashborad监测各个服务处理情况

# springcloud_student_zuul_3001
## 是路由网关，用来提高系统访问安全性能
## 配置方式
```
server:
  port: 3001
  context-path: /
# 应用实例名称
spring:
  application:
    name: student-zuul

# 配置注册中心，路由网关需要注册到注册中心 否则无法通过网关进行调用其它服务
eureka:
  instance:
    instance-id: student-zuul:3001 #客户端实例名称
    prefer-ip-address: true #显示IP
  client:
    service-url:
      defaultZone: http://eureka2001.etjava.com:2001/eureka/,http://eureka2002.etjava.com:2002/eureka/,http://eureka2003.etjava.com:2003/eureka/ # 集群

# 配置路由规则 屏蔽直接通过应用实例名称直接调用服务
zuul:
      # 屏蔽通过服务实例名称进行访问 如果屏蔽单个服务实例 直接写服务实例名称，多个使用星号表示
  # 只屏蔽某一个服务  ignored-services: "student-provider"
  # 屏蔽某一个服务后访问时404代替
  # 屏蔽多个 即 ignored-services: "*" 会自动映射成指定的path访问路径 不会使用404
  ignored-services: "*"
  prefix: /etjava # 指定请求的前缀 如果指定了访问时的前缀 在访问时必须带上 否则404
  routes:
    # studentServer是随意取的 但后边的serviceId及path是固定写法
    # serviceId 指定要进行映射的服务名称 path是映射成的访问路径
    studentServer.serviceId: student-provider # 服务提供者的实例名称
    studentServer.path: /studentServer/** # 映射后的请求服务名称 隐藏原有的服务实例名称



info:
   groupId: $project.groupId$
   artifactId: $project.artifactId$
   version: $project.version$
   负责人: 王五
   联系电话: 123456
```
# springcloud_config_server_6001
## 是springcloud-config 利用它可以实现将项目的配置文件与项目本身分离 这样做的目的是为了便于管理各个项目之间的配置文件
## server端配置只需要指向远程库中存放各种配置文件的地址即可
### 注意: 不能带有后缀(.git)

## 服务消费者需要启动时需要先在bootstrap.yml中进行配置要去读的远程库中的文件
## 服务消费者不需要再从application.yml中继续配置繁琐的各种配置项，只要远程库中的配置文件中存在 则都会在项目启动时注入进来
### 服务端的配置如下
```
server:
  port: 6001
  context-path: /

spring:
  application:
    name:  student-config
  # 配置springcloud的config server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/etjava/springcloud-config-test # uri指向的是git仓库中的地址 注意不能带有后缀 即 .git
```
### 消费端的配置如下
**bootstrap.yml**
```
spring:
  application:
    name: consumer-dev
  cloud:
    config:
    # name和profile
      name: consumer_config2
      uri: http://localhost:6001/  # 配置configserver地址
      profile: dev  # 级别
      label: master  # 分支 git中 默认master
      fail-fast: true
```
**application.yml**
```
spring:
  application:
    name: student-consumer-config


# 集群版 STUDENT-PROVIDER22 对应的是服务提供者配置文件中的application.name的值
# 这里只能使用中划线 不能使用下划线 提供者那边也只能使用中划线
provider_host: http://MICROSERVICE-STUDENT
```


