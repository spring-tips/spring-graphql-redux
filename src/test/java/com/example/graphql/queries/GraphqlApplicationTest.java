package com.example.graphql.queries;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;


@SpringBootTest
class GraphqlApplicationTest {

    @Test
    void customerById() {
        var client = WebTestClient.bindToServer ()
                .baseUrl("http://localhost:8080/graphql")
                .build();
        var tester = HttpGraphQlTester.create(client);
        var document = """
                 {
                   customerById(id: 1) {
                     id
                     name
                   }
                 }
                """;
        tester.document(document).execute().path("customerById").entity(Customer.class ).matches( c -> c.name().equals("Rossen"));

    }


}