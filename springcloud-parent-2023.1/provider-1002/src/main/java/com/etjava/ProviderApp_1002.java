package com.etjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ProviderApp_1002 {

	public static void main(String[] args) {
		SpringApplication.run(ProviderApp_1002.class, args);
	}
}
