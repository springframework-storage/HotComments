package com.naver.hackday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "com.naver.hackday")
public class HackdayApplication {

	public static void main(String[] args) {
		SpringApplication.run(HackdayApplication.class, args);
	}
}
