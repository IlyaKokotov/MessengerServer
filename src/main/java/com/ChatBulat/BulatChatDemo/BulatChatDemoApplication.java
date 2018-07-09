package com.ChatBulat.BulatChatDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BulatChatDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulatChatDemoApplication.class, args);
	}
}
