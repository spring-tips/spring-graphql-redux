package com.example.queries;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
class QueriesApplicationTests {

    @Test
    void contextLoads() {
        var client = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080/graphql")
                .build();
        var tester = HttpGraphQlTester
                .create(client);
        var document = """
                {
                  customerById (id: 1) {
                   id, name
                  } 
                }
                """;
        tester
                .document(document)
                .execute()
                .path("customerById")
                .entity(Customer.class)
                .matches(customer -> customer.name().equals("A"));
    }

}
