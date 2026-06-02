package com.ca.ca_server;

import com.ca.ca_server.config.CASignatureConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CASignatureConfig.class)
public class CaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaServerApplication.class, args);
	}

}
