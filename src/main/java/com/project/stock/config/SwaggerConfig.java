package com.project.stock.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI(@Value("${springdoc.swagger-ui.server.url}") String serverUrl,
			@Value("${springdoc.swagger-ui.server.description}") String serverDescription) {

		Info info = new Info().title("Stock Ranking API")
				.version("v1.0.0")
				.description("주식 종목을 인기순, 가격변동률, 거래량에 따라 실시간 순위를 제공하는 API");

		return new OpenAPI()
				.components(new Components())
				.servers(List.of(new Server()
						.url(serverUrl)
						.description(serverDescription)))
				.info(info);
	}

}
