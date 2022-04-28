package com.example.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }

}

@Controller
class BatchController {

    @QueryMapping
    Collection<Customer> customers() {
        return List.of(new Customer(1, "A"), new Customer(2, "B"));
    }

    @BatchMapping
    Map<Customer, Account> account(List<Customer> customers) {
        System.out.println("calling account for " + customers.size() + " customers.");
        return customers
                .stream()
                .collect(Collectors.toMap(customer -> customer,
                        customer -> new Account(customer.id())));
    }

/*    @SchemaMapping(typeName = "Customer")
    Account account(Customer customer) {
        System.out.println("getting account for customer # " + customer.id());
        return new Account(customer.id());
    }*/

}

record Account(Integer id) {
}

record Customer(Integer id, String name) {
}