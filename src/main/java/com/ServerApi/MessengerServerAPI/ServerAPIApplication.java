package com.ServerApi.MessengerServerAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ServerAPIApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerAPIApplication.class, args);
	}
}
