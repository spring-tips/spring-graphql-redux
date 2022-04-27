package com.example.graphql.engine;

import graphql.schema.DataFetchingEnvironment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@SpringBootApplication
public class GraphqlApplication {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "engine");
        SpringApplication.run(GraphqlApplication.class, args);
    }

    @Bean
    RuntimeWiringConfigurer runtimeWiringConfigurer(CrmService crmService) {
        return builder -> {
            builder.type("Customer", wiring -> wiring
                    .dataFetcher("profile", environment -> crmService.getProfileFor(environment.getSource())));
            builder.type("Query", wiring -> wiring
                    .dataFetcher("customers", env -> crmService.getCustomers())
                    .dataFetcher("customerById", env -> crmService.getCustomerById(getId(env))));
        };
    }

    private int getId(DataFetchingEnvironment e) {
        return Integer.parseInt(e.getArgument("id"));
    }

}

record Profile(Integer id, Integer customerId) {
}

record Customer(Integer id, String name) {
}

@Service
class CrmService {

    private final Map<Integer, Customer> customers = Map.of(
            1, new Customer(1, "A"),
            2, new Customer(2, "B"));

    Profile getProfileFor(Customer customer) {
        return  new Profile( customer.id(), customer.id()) ;
    }

    Collection<Customer> getCustomers() {
        return this.customers.values();
    }

    Customer getCustomerById(Integer id) {
        return this.customers.get(id);
    }

}