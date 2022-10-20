package com.etjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaApp_2003 {

	public static void main(String[] args) {
		SpringApplication.run(EurekaApp_2003.class, args);
	}
}
