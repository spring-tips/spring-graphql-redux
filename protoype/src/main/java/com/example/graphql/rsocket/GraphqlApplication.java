package com.example.graphql.rsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Stream;

@SpringBootApplication
public class GraphqlApplication {

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "rsocket");
		SpringApplication.run(GraphqlApplication.class, args);
	}

}

@Controller
class CrmRSocketGraphqlController {

	@SubscriptionMapping
	Flux<Map<String, String>> greetings() {
		return Flux
				.fromStream(
						Stream.generate(() -> Map.of("greeting", "Hello, world @ " + Instant.now().toString() + "!")))
				.delayElements(Duration.ofSeconds(1)).take(10);
	}

	@QueryMapping
	String hello() {
		return "Hello, world!";
	}

}
