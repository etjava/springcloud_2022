# Spring Cloud 练习 DEMO
## Module description

### springcloud_student_parent 所有模块的父模块 用来管理其他module,依赖及其他相关信息(例如 各个模块之间的编译版本,统一编码等)

### springcloud_student_common 公共模块 用来存放JavaBean,各种工具类等

### springcloud_student_provider 服务提供者模块 生产消息发布到注册中心
-- 服务提供者在接收对象参数时需要使用@RequestBody标注 例如 (@RequestBody Student student)

### springcloud_student-consumer 服务消费者 远程注册中心的服务
-- 通过RestTemplate进行对远程服务调用

## Eureka1.x 服务注册中心搭建