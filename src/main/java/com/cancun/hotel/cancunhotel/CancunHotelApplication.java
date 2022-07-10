package com.cancun.hotel.cancunhotel;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDate;

@SpringBootApplication
public class CancunHotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(CancunHotelApplication.class, args);
	}

	@Bean
	public Docket swaggerCancunHotelApi(TypeResolver typeResolver) {
		return new Docket(DocumentationType.SWAGGER_2)
				.additionalModels(
						typeResolver.resolve(LocalDate.class)
				)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.cancun.hotel.cancunhotel.controller"))
				.build()
				.useDefaultResponseMessages(false)
				.apiInfo(new ApiInfoBuilder().version("1.0").title("Cancun Hotel Application").description("REST-Api to manage bookings").build());
	}
}
