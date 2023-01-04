package com.etjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker // 开启熔断
public class ProviderApp_1005 {

	public static void main(String[] args) {
		SpringApplication.run(ProviderApp_1005.class, args);
	}
}
