package com.backend.Wasteless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class WastelessApplication {

	public static void main(String[] args) {

		SpringApplication.run(WastelessApplication.class, args);
	}

}
