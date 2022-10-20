package com.etjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer // 开启configServer
public class SprngCloudConfigApp_6001 {

	public static void main(String[] args) {
		SpringApplication.run(SprngCloudConfigApp_6001.class, args);
	}
}
