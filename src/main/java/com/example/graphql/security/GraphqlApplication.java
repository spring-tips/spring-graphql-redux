package com.example.graphql.security;

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

/**
 * See the {@code README.md} for examples on how to use {@code curl} to talk to these
 * endpoints authenticated
 */
@EnableReactiveMethodSecurity
@SpringBootApplication
public class GraphqlApplication {

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "security");
		SpringApplication.run(GraphqlApplication.class, args);
	}

	@Bean
	SecurityWebFilterChain authorization(ServerHttpSecurity http) {
		return http.csrf(ServerHttpSecurity.CsrfSpec::disable).authorizeExchange(ae -> ae.anyExchange().permitAll())
				.httpBasic(Customizer.withDefaults()).build();
	}

	@Bean
	MapReactiveUserDetailsService authentication() {
		var users = Map.of("jlong", new String[] { "USER" }, "rwinch", "ADMIN,USER".split(",")).entrySet().stream()
				.map(entry -> User.withDefaultPasswordEncoder().username(entry.getKey()).roles(entry.getValue())
						.password("pw").build())
				.toList();
		return new MapReactiveUserDetailsService(users);
	}

}

@Controller
class CrmGraphqlController {

	private final CrmService crm;

	CrmGraphqlController(CrmService crm) {
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

	private final Map<Integer, Customer> db = new ConcurrentHashMap<>();

	private final AtomicInteger id = new AtomicInteger();

	@Secured("ROLE_VIEWER")
	public Mono<Customer> getCustomerById(Integer id) {
		return Mono.just(this.db.get(id));
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
