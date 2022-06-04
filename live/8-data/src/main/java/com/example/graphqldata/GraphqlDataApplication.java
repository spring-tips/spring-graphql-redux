package com.example.graphqldata;

import com.querydsl.core.annotations.QueryEntity;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class GraphqlDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlDataApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(CustomerRepository repository) {
        return args -> repository
                .deleteAll()
                .thenMany(Flux.just("Josh", "Jürgen", "Dave", "Madhura", "Mark", "Yuxin").map(name -> new Customer(null, name)).flatMap(repository::save))
                .thenMany(repository.findAll(QCustomer.customer.name.startsWith("J").not()))
                .subscribe(System.out::println);
    }


}

record Account(String id) {
}

@Controller
class AccountController {

    @BatchMapping
    Map<Customer, Account> account(List<Customer> customers) {
        var map = new HashMap<Customer, Account>();
        for (var c : customers)
            map.put(c, new Account(c.id()));
        return map;
    }

}

@GraphQlRepository
interface CustomerRepository extends ReactiveCrudRepository<Customer, String>,
        ReactiveQuerydslPredicateExecutor<Customer> {
}

@QueryEntity
@Document
record Customer(@Id String id, String name) {
}


