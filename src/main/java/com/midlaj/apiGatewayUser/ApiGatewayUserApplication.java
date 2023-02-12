package com.midlaj.apiGatewayUser;

import com.midlaj.apiGatewayUser.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class ApiGatewayUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayUserApplication.class, args);
	}

}
