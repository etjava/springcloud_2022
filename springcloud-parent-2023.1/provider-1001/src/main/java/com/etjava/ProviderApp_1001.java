package com.etjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ProviderApp_1001 {

	public static void main(String[] args) {
		SpringApplication.run(ProviderApp_1001.class, args);
	}
}
