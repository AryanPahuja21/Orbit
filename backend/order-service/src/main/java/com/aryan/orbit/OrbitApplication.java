package com.aryan.orbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
public class OrbitApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrbitApplication.class, args);
	}

}
