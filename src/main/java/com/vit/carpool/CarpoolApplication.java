package com.vit.carpool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "com.vit.carpool.jwt") // Add the package here
public class CarpoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarpoolApplication.class, args);
	}
}
