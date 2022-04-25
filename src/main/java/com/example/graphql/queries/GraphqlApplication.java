package com.example.graphql.queries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@SpringBootApplication
public class GraphqlApplication {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "queries");
        SpringApplication.run(GraphqlApplication.class, args);
    }
}

@Controller
class GreetingsController {

    private String greet(String name) {
        return "Hello, " + name + "!";
    }

    @QueryMapping
    String hello() {
        return greet("World");
    }

    @QueryMapping
    String helloWithName(@Argument String name) {
        return greet(name);
    }

    //	@SchemaMapping (typeName = "Query", field = "customerById")
    @QueryMapping
    Customer customerById(@Argument Integer id) {
        return new Customer(id, "Rossen");
    }

    @QueryMapping
    Flux<Customer> customers() {
        return Flux.just(new Customer(1, "A"), new Customer(2, "B"));
    }

    @SchemaMapping(typeName = "Customer")
    Mono<Account> account(Customer customer) {
        return Mono.just(new Account(customer.id(), new Date()));
    }
}

record Customer(Integer id, String name) {
}

record Account(Integer id, Date signup) {
}