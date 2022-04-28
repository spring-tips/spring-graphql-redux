package com.example.queries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootApplication
public class QueriesApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueriesApplication.class, args);
    }

}

@Controller
class GreetingsController {

    @QueryMapping
    Customer customerById(@Argument Integer id) {
        return new Customer(id, Math.random() > .5 ? "A" : "B");
    }

    private final List<Customer> customerList =
            List.of(new Customer(1, "A"), new Customer(2, "B"));

    @QueryMapping
    String helloWithName(@Argument String name) {
        return "Hello, " + name + "!";
    }

    @QueryMapping
//    @SchemaMapping(typeName = "Query", field = "hello")
    String hello() {
        return "Hello, world!";
    }

    @QueryMapping
    Flux<Customer> customers() {
        return Flux.fromIterable(this.customerList);
    }

    @SchemaMapping(typeName = "Customer")
    Mono<Account> account(Customer customer) {
        return Mono.just(new Account(customer.id()));
    }

}

record Account(Integer id) {
}

record Customer(Integer id, String name) {
}