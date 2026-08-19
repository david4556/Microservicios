package com.david.servicioa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicioaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioaApplication.class, args);
	}

}
