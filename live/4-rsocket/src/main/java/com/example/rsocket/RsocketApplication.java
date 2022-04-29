package com.example.rsocket;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@SpringBootApplication
public class RsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(RsocketApplication.class, args);
    }

}

@Controller
class GreetingsController {

    @SubscriptionMapping
    Flux<Greeting> greetings() {
        return Flux
                .fromStream(Stream.generate(() -> new Greeting("Hello, world @ " + Instant.now() + "!")))
                .delayElements(Duration.ofSeconds(1))
                .take(10);
    }

    @QueryMapping
    Greeting greeting() {
        return new Greeting("Hello, world!");
    }
}

record Greeting(String greeting) {
}