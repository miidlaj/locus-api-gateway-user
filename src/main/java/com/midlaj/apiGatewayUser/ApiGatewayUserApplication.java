package com.midlaj.apiGatewayUser;

import com.midlaj.apiGatewayUser.config.AppProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableFeignClients
@OpenAPIDefinition(
		info = @Info(
				title = "API GATEWAY FOR USER",
				version = "1.0",
				description = "This project is gateway for Users",
				contact = @Contact(
						name = "Muhammed Midlaj",
						email = "mumidlaj@gmail.com"
				),
				license = @License(
						name = "license",
						url = "license.com"
				)
		)
)
public class ApiGatewayUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayUserApplication.class, args);
	}

}
