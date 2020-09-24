package com.jzarob.docmm4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class Docmm4jApplication {

	public static void main(String[] args) {
		SpringApplication.run(Docmm4jApplication.class, args);
	}
}
