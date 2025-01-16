package com.study.ex_spring_batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ExSpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExSpringBatchApplication.class, args);
	}

}
