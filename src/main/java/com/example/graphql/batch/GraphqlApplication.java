package com.example.graphql.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class GraphqlApplication {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "batch");
        SpringApplication.run(GraphqlApplication.class, args);
    }
}

@Controller
class CrmGraphqlController {

    private final CrmService crmService;

    CrmGraphqlController(CrmService crmService) {
        this.crmService = crmService;
    }

    @QueryMapping
    Collection<Customer> customers() {
        return crmService.getCustomers();
    }

    @BatchMapping
    Map<Customer, List<Order>> orders(List<Customer> customers) {
        return this.crmService.getOrdersForCustomers(customers);
    }
}

@Service
class CrmService {

    Map<Customer, List<Order>> getOrdersForCustomers(List<Customer> customers) {
        return customers
                .stream()
                .collect(Collectors.toMap(
                        customer -> customer,
                        customer -> List.of(new Order(1, customer.id()),
                                new Order(2, customer.id()))));
    }

    Collection<Customer> getCustomers() {
        return List.of(new Customer(1, "Madhura"),
                new Customer(2, "Yuxin"));
    }

}

record Customer(Integer id, String name) {
}

record Order(Integer id, Integer customerId) {
}