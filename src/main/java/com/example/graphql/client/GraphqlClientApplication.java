package com.example.graphql.client;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.RSocketGraphQlClient;

/**
 * Be sure to start both the {@code queries} example and the {@code rsocket} example before running this
 */
@SpringBootApplication
public class GraphqlClientApplication {

    public static void main(String[] args) throws Exception {
        System.setProperty("spring.profiles.active", "client");
        SpringApplication.run(GraphqlClientApplication.class, args);
    }

    @Bean
    RSocketGraphQlClient rSocketGraphQlClient(RSocketGraphQlClient.Builder<?> builder) {
        return builder.tcp("127.0.0.1", 9191).route("graphql").build();
    }

    @Bean
    HttpGraphQlClient graphQlClient() {
        return HttpGraphQlClient
                .builder()
                .url("http://127.0.0.1:8080/graphql")
                .build();
    }

    @Bean
    ApplicationRunner applicationRunner(HttpGraphQlClient httpGraphQlClient,
                                        RSocketGraphQlClient rSocketGraphQlClient) {
        return args -> {
            var httpDocument = """
                    query {
                       customerById(id:1) {
                         id, name
                      }
                    }
                    """;
            httpGraphQlClient
                    .document(httpDocument)
                    .retrieve("customerById")
                    .toEntity(Customer.class)
                    .subscribe(System.out::println);

            var rsocketDocument = """
                        subscription {
                         greetings { greeting }
                        }
                    """;
            rSocketGraphQlClient
                    .document(rsocketDocument)
                    .retrieveSubscription("greetings")
                    .toEntity(Greeting.class)
                    .subscribe(System.out::println);

        };
    }
}

record Greeting(String greeting) {
}

record Customer(Integer id, String name) {
}