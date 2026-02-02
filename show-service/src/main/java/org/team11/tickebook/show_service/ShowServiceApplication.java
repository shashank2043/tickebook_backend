package org.team11.tickebook.show_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ShowServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShowServiceApplication.class, args);
	}

}
