package com.example.graphql.mutations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class GraphqlApplication {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "mutations");
        SpringApplication.run(GraphqlApplication.class, args);
    }
}

@Controller
class MutationController {

    private final Map<Integer, Customer> db = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    @QueryMapping
    Customer customerById (@Argument Integer id) {
        return this.db.get (id );
    }

    @MutationMapping
    Customer insert(@Argument String name) {
        var id = this.id.incrementAndGet();
        var value = new Customer(id, name);
        this.db.put(id, value);
        return value;
    }
}

record Customer(Integer id, String name) {
}
