package com.example.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@EnableReactiveMethodSecurity
@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Bean
    MapReactiveUserDetailsService authentication() {
        var users = Map.of(
                        "jlong", new String[]{"USER"},
                        "rwinch", "ADMIN,USER".split(","))
                .entrySet()
                .stream()
                .map(e -> User.withDefaultPasswordEncoder()
                        .username(e.getKey())
                        .password("pw")
                        .roles(e.getValue())
                        .build()
                )
                .toList();
        return new MapReactiveUserDetailsService(users);
    }

    @Bean
    SecurityWebFilterChain authorization(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ae -> ae.anyExchange().permitAll())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}


@Controller
class SecureGraphqlController {


    private final CrmService crm;

    SecureGraphqlController(CrmService crm) {
        this.crm = crm;
    }

    @MutationMapping
    Mono<Customer> insert(@Argument String name) {
        return this.crm.insert(name);
    }

    @QueryMapping
    Mono<Customer> customerById(@Argument Integer id) {
        return this.crm.getCustomerById(id);
    }
}

@Service
class CrmService {

    private final Map<Integer, Customer> db =
            new ConcurrentHashMap<>();

    private final AtomicInteger id = new AtomicInteger();


    @Secured("ROLE_USER")
    public Mono<Customer> getCustomerById(Integer id) {
        var customer = this.db.get(id);
        return Mono.just(customer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Customer> insert(String name) {
        var newCustomer = new Customer(id.incrementAndGet(), name);
        this.db.put(newCustomer.id(), newCustomer);
        return Mono.just(newCustomer);
    }
}

record Customer(Integer id, String name) {
}