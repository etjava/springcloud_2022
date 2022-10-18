package com.etjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class StudentProviderApp {

	public static void main(String[] args) {
		SpringApplication.run(StudentProviderApp.class, args);
	}
}
