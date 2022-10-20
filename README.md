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
