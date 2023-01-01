# springcloud 完整实例
# 目录介绍
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

