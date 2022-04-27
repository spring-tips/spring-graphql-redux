package com.example.graphql.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Turns off authorization for everything <em>unless</em> somebody specifies the property
 * to enable it. Makes it easier to introduce the feature as we need it, but have it
 * backoff for earlier work.
 *
 * @author Josh Long
 */
@Configuration
@AutoConfigureBefore(SecurityAutoConfiguration.class)
@ConditionalOnProperty(prefix = "bootiful.graphql.security",
        name = "enabled", havingValue = "false", matchIfMissing = true)
class GraphqlSecurityAutoConfiguration {

    @Bean
    SecurityWebFilterChain securityWebFilterChain() {
        return new SecurityWebFilterChain() {
            @Override
            public Mono<Boolean> matches(ServerWebExchange exchange) {
                return Mono.just(false);
            }

            @Override
            public Flux<WebFilter> getWebFilters() {
                return Flux.empty();
            }
        };
    }
}
