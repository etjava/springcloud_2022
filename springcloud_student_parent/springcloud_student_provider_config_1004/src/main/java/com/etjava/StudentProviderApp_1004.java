package com.etjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class StudentProviderApp_1004 {

	public static void main(String[] args) {
		SpringApplication.run(StudentProviderApp_1004.class, args);
	}
}
