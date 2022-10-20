package com.etjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@EnableCircuitBreaker // 开启熔断保护机制
@EnableEurekaClient
@SpringBootApplication
public class StudentProviderHystrixApp_1005 {

	public static void main(String[] args) {
		SpringApplication.run(StudentProviderHystrixApp_1005.class, args);
	}
}
