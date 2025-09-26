package com.example.proapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ProappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProappApplication.class, args);
	}

}
