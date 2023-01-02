package com.etjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp_2000 {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApp_2000.class, args);
	}
}
