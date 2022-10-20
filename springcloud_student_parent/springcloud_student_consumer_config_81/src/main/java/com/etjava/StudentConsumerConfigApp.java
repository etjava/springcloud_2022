package com.etjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * exclude 表示不包含
 * 	由于服务消费者模块不需要直接操作数据库 因此不需要注入数据源
 * 
 * @EnableEurekaClient 开启eureka客户端
 * @author etjav
 *
 */
@EnableEurekaClient
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class StudentConsumerConfigApp {

	public static void main(String[] args) {
		SpringApplication.run(StudentConsumerConfigApp.class, args);
	}
}
