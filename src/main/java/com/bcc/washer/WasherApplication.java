package com.bcc.washer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.bcc.washer.config.SecurityConfig;
@SpringBootApplication
@Import(SecurityConfig.class)
public class WasherApplication {

	public static void main(String[] args) {
		SpringApplication.run(WasherApplication.class, args);
	}

}