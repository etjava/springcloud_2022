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


